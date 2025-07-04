package com.example.taskmanager.app.services;

import com.example.taskmanager.app.dtos.attributes.CreateTaskAttributeRequestDTO;
import com.example.taskmanager.app.dtos.attributes.UpdateTaskAttributeRequestDTO;
import com.example.taskmanager.app.dtos.node.CreateNodeRequestDTO;
import com.example.taskmanager.app.dtos.node.NodeResponseDTO;
import com.example.taskmanager.app.dtos.node.UpdateNodeRequestDTO;
import com.example.taskmanager.app.entities.*;
import com.example.taskmanager.app.mappers.NodeMapper;
import com.example.taskmanager.app.repositories.NodeRepository;
import com.example.taskmanager.app.repositories.TaskStatusRepository;
import com.example.taskmanager.app.repositories.TaskTypeRepository;
import com.example.taskmanager.user.entities.User;
import com.example.taskmanager.user.services.AuthService;
import com.example.taskmanager.utils.exceptions.UnauthorizedAccessException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NodeService {
    private final NodeRepository nodeRepository;
    private final NodeMapper nodeMapper;
    private final AuthService authService;
    private final TaskStatusRepository taskStatusRepository;
    private final TaskTypeRepository taskTypeRepository;

    private List<TaskStatus> createDefaultTaskStatuses(Node node) {
        List<String> names = List.of("New task", "Scheduled", "In progress", "Review", "Completed");
        List<String> colors = List.of("blue", "purple", "orange", "rose", "green");

        List<TaskStatus> taskStatuses = new ArrayList<>();

        for (String name : names) {
            TaskStatus taskStatus = TaskStatus.builder()
                    .name(name)
                    .color(ColorTypesEnum.valueOf(colors.get(names.indexOf(name))))
                    .orderIndex(name.equals("Completed") ? null : (names.indexOf(name) + 1) * 10000L)
                    .kind(name.equals("Completed") ? TaskStatusKindEnum.Completed : TaskStatusKindEnum.Custom)
                    .deletable(!name.equals("Completed"))
                    .draggable(!name.equals("Completed"))
                    .node(node)
                    .build();

            taskStatuses.add(taskStatus);
        }

        return taskStatusRepository.saveAll(taskStatuses);
    }

    private List<TaskType> createDefaultTaskTypes(Node node) {
        List<String> names = List.of("Operational", "Technical", "Strategic", "Hiring", "Financial");
        List<String> colors = List.of("blue", "indigo", "orange", "lime", "green");

        List<TaskType> taskTypes = new ArrayList<>();

        for (String name : names) {
            TaskType taskType = TaskType.builder()
                    .name(name)
                    .color(ColorTypesEnum.valueOf(colors.get(names.indexOf(name))))
                    .orderIndex((names.indexOf(name) + 1) * 10000L)
                    .node(node)
                    .build();

            taskTypes.add(taskType);
        }

        return taskTypeRepository.saveAll(taskTypes);
    }

    public NodeResponseDTO createNodeInRoot(NodeKindEnum kind, CreateNodeRequestDTO request) {
        String name = request.name().trim();

        User user = authService.getAuthenticatedUser();

        Long maxRank = nodeRepository.findMaxRankForRootNodes(user.getId()).orElse(0L);
        Long newRank = maxRank + 10000L;

        Node node = nodeRepository.save(Node.builder()
                        .name(name)
                        .user(user)
                        .rank(newRank)
                        .kind(kind)
                        .build());

        if (kind.equals(NodeKindEnum.Project)) {
            List<TaskStatus> taskStatuses = createDefaultTaskStatuses(node);
            node.setTaskStatuses(taskStatuses);

            List<TaskType> taskTypes = createDefaultTaskTypes(node);
            node.setTaskTypes(taskTypes);
        }

        return nodeMapper.nodeToNodeResponseDTO(node);
    }

    public NodeResponseDTO createNode(UUID parentNodeId, CreateNodeRequestDTO request) {
        String name = request.name().trim();

        User user = authService.getAuthenticatedUser();

        Node parentNode = nodeRepository.findById(parentNodeId)
                .orElseThrow(() -> new RuntimeException("Node not found with id: " + parentNodeId));

        if (!parentNode.getUser().getId().equals(user.getId())) {
            throw new UnauthorizedAccessException("You don't have permission to create a project in this folder");
        }

        Long maxRank = nodeRepository.findMaxRankByParentNodeId(parentNodeId).orElse(0L);
        Long newRank = maxRank + 10000L;

        Node node = nodeRepository.save(Node.builder()
                .name(name)
                .user(user)
                .rank(newRank)
                .kind(NodeKindEnum.Project)
                .parentNode(parentNode)
                .build());

        List<TaskStatus> taskStatuses = createDefaultTaskStatuses(node);
        node.setTaskStatuses(taskStatuses);

        List<TaskType> taskTypes = createDefaultTaskTypes(node);
        node.setTaskTypes(taskTypes);

        parentNode.getChildren().add(node);

        return nodeMapper.nodeToNodeResponseDTO(node);
    }

    public NodeResponseDTO createNewTaskStatus(UUID nodeId, CreateTaskAttributeRequestDTO request) {
        String name = request.name().trim();
        ColorTypesEnum color = ColorTypesEnum.valueOf(request.color());

        User user = authService.getAuthenticatedUser();

        Node node = nodeRepository.findById(nodeId)
                .orElseThrow(() -> new RuntimeException("Node not found with id: " + nodeId));

        if (!node.getUser().getId().equals(user.getId())) {
            throw new UnauthorizedAccessException("You don't have permission to access this node");
        }

        Long maxOrder = taskStatusRepository.findMaxOrderIndexByNodeId(nodeId).orElse(0L);
        Long newOrder = maxOrder + 10000L;

        TaskStatus taskStatus = TaskStatus.builder()
                .name(name)
                .orderIndex(newOrder)
                .color(color)
                .node(node)
                .kind(TaskStatusKindEnum.Custom)
                .draggable(true)
                .deletable(true)
                .build();

        node.getTaskStatuses().add(taskStatus);
        nodeRepository.save(node);

        return nodeMapper.nodeToNodeResponseDTO(node);
    }

    public NodeResponseDTO createNewTaskType(UUID nodeId, CreateTaskAttributeRequestDTO request) {
        String name = request.name().trim();
        ColorTypesEnum color = ColorTypesEnum.valueOf(request.color());

        User user = authService.getAuthenticatedUser();

        Node node = nodeRepository.findById(nodeId)
                .orElseThrow(() -> new RuntimeException("Node not found with id: " + nodeId));

        if (!node.getUser().getId().equals(user.getId())) {
            throw new UnauthorizedAccessException("You don't have permission to access this node");
        }

        Long maxOrder = taskTypeRepository.findMaxOrderIndexByNodeId(nodeId).orElse(0L);
        Long newOrder = maxOrder + 10000L;

        TaskType taskType = TaskType.builder()
                .name(name)
                .orderIndex(newOrder)
                .color(color)
                .node(node)
                .build();

        node.getTaskTypes().add(taskType);
        nodeRepository.save(node);

        return nodeMapper.nodeToNodeResponseDTO(node);
    }

    public List<NodeResponseDTO> getNodeStructure() {
        User user = authService.getAuthenticatedUser();
        List<Node> nodeStructure = nodeRepository.findAllByUserId(user.getId());
        return nodeStructure.stream()
                .map(nodeMapper::nodeToNodeResponseDTO)
                .collect(Collectors.toList());
    }

    public NodeResponseDTO getNodeById(UUID id) {
        User user = authService.getAuthenticatedUser();
        Node node = nodeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Node not found with id: " + id));
        
        if (!node.getUser().getId().equals(user.getId())) {
            throw new UnauthorizedAccessException("You don't have permission to access this node");
        }
        
        return nodeMapper.nodeToNodeResponseDTO(node);
    }

    public NodeResponseDTO updateNodeById(UUID id, UpdateNodeRequestDTO request) {
        String newName = request.name().trim();
        Long newRank = request.rank();
        UUID parentNodeId = request.parentNodeId();

        User user = authService.getAuthenticatedUser();
        Node node = nodeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Node not found with id: " + id));

        if (!node.getUser().getId().equals(user.getId())) {
            throw new UnauthorizedAccessException("You don't have permission to update this node");
        }

        if (!newName.equals(node.getName())) {
            node.setName(newName);
        }

        if (newRank != null && !newRank.equals(node.getRank())) {
            node.setRank(newRank);
        }

        if (parentNodeId == null) {
            node.setParentNode(null);
        } else if (!parentNodeId.equals(node.getParentNode().getId())) {
            Node parentNode = nodeRepository.findById(parentNodeId).orElse(null);

            if (parentNode != null && !parentNode.getUser().getId().equals(user.getId())) {
                throw new UnauthorizedAccessException("You don't have permission to access this node");
            }

            node.setParentNode(parentNode);
        }

        Node updatedNode = nodeRepository.save(node);
        
        return nodeMapper.nodeToNodeResponseDTO(updatedNode);
    }

    public NodeResponseDTO updateTaskStatus(UUID nodeId, UUID taskStatusId, UpdateTaskAttributeRequestDTO request) {
        String name = request.name().trim();
        ColorTypesEnum color = request.color();
        Long orderIndex = request.orderIndex();

        User user = authService.getAuthenticatedUser();

        Node node = nodeRepository.findById(nodeId)
                .orElseThrow(() -> new RuntimeException("Node not found with id: " + nodeId));

        if (!node.getUser().getId().equals(user.getId())) {
            throw new UnauthorizedAccessException("You don't have permission to access this node");
        }

        TaskStatus taskStatus = node.getTaskStatuses().stream()
                .filter(ts -> ts.getId().equals(taskStatusId))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("task_status not found with id: " + taskStatusId));


        if (!name.equals(taskStatus.getName())) {
            taskStatus.setName(name);
        }

        if (!color.equals(taskStatus.getColor())) {
            taskStatus.setColor(color);
        }

        if (orderIndex != null && !orderIndex.equals(taskStatus.getOrderIndex())) {
            if (taskStatus.getKind().equals(TaskStatusKindEnum.Completed)) {
                throw new RuntimeException("You can't change order index of completed task status");
            } else {
                taskStatus.setOrderIndex(orderIndex);
            }
        }

        taskStatusRepository.save(taskStatus);

        return nodeMapper.nodeToNodeResponseDTO(node);
    }

    public NodeResponseDTO updateTaskType(UUID nodeId, UUID taskTypeId, UpdateTaskAttributeRequestDTO request) {
        String name = request.name().trim();
        ColorTypesEnum color = request.color();
        Long orderIndex = request.orderIndex();

        User user = authService.getAuthenticatedUser();

        Node node = nodeRepository.findById(nodeId)
                .orElseThrow(() -> new RuntimeException("Node not found with id: " + nodeId));

        if (!node.getUser().getId().equals(user.getId())) {
            throw new UnauthorizedAccessException("You don't have permission to access this node");
        }

        TaskType taskType = node.getTaskTypes().stream()
                .filter(tt -> tt.getId().equals(taskTypeId))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("task_type not found with id: " + taskTypeId));


        if (!name.equals(taskType.getName())) {
            taskType.setName(name);
        }

        if (!color.equals(taskType.getColor())) {
            taskType.setColor(color);
        }

        if (orderIndex != null && !orderIndex.equals(taskType.getOrderIndex())) {
            taskType.setOrderIndex(orderIndex);
        }

        taskTypeRepository.save(taskType);

        return nodeMapper.nodeToNodeResponseDTO(node);
    }

    public void deleteTaskStatus(UUID nodeId, UUID taskStatusId) {
        User user = authService.getAuthenticatedUser();
        Node node = nodeRepository.findById(nodeId)
                .orElseThrow(() -> new RuntimeException("Node not found with id: " + nodeId));

        if (!node.getUser().getId().equals(user.getId())) {
            throw new UnauthorizedAccessException("You don't have permission to access this node");
        }

        TaskStatus taskStatus = node.getTaskStatuses().stream()
                .filter(ts -> ts.getId().equals(taskStatusId))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("task_status not found with id: " + taskStatusId));

        if (taskStatus.getKind().equals(TaskStatusKindEnum.Completed)) {
            throw new RuntimeException("You can't delete completed task status");
        } else {
            node.getTaskStatuses().remove(taskStatus);
            taskStatusRepository.delete(taskStatus);
        }
        nodeRepository.save(node);
    }

    public void deleteTaskType(UUID nodeId, UUID taskTypeId) {
        User user = authService.getAuthenticatedUser();
        Node node = nodeRepository.findById(nodeId)
                .orElseThrow(() -> new RuntimeException("Node not found with id: " + nodeId));

        if (!node.getUser().getId().equals(user.getId())) {
            throw new UnauthorizedAccessException("You don't have permission to access this node");
        }

        TaskType taskType = node.getTaskTypes().stream()
                .filter(tt -> tt.getId().equals(taskTypeId))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("task_type not found with id: " + taskTypeId));

        node.getTaskTypes().remove(taskType);
        taskTypeRepository.delete(taskType);
        nodeRepository.save(node);
    }

    public void deleteNodeById(UUID id) {
        User user = authService.getAuthenticatedUser();
        Node node = nodeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Node not found with id: " + id));

        if (!node.getUser().getId().equals(user.getId())) {
            throw new UnauthorizedAccessException("You don't have permission to delete this node");
        }

        nodeRepository.delete(node);
    }
}
