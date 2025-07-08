package com.example.taskmanager.app.controllers;

import com.example.taskmanager.app.dtos.attributes.CreateTaskAttributeRequestDTO;
import com.example.taskmanager.app.dtos.attributes.UpdateTaskAttributeRequestDTO;
import com.example.taskmanager.app.dtos.node.NodeResponseDTO;
import com.example.taskmanager.app.dtos.node.CreateNodeRequestDTO;
import com.example.taskmanager.app.dtos.node.UpdateNodeRequestDTO;
import com.example.taskmanager.app.entities.NodeKindEnum;
import com.example.taskmanager.app.services.NodeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/nodes")
@RequiredArgsConstructor
public class NodeController {
    private final NodeService nodeService;

    @PostMapping
    public ResponseEntity<NodeResponseDTO> createNodeInRoot(@RequestParam(name = "kind", defaultValue = "Project") NodeKindEnum kind, @Valid @RequestBody CreateNodeRequestDTO request) {
        return new ResponseEntity<>(nodeService.createNodeInRoot(kind, request), HttpStatus.CREATED);
    }

    @PostMapping("/{parentId}/projects")
    public ResponseEntity<NodeResponseDTO> createNode(@PathVariable UUID parentId, @Valid @RequestBody CreateNodeRequestDTO request) {
        return new ResponseEntity<>(nodeService.createNode(parentId, request), HttpStatus.CREATED);
    }

    @PostMapping("/{id}/task-statuses")
    public ResponseEntity<NodeResponseDTO> createNewTaskStatuses(@PathVariable UUID id, @Valid @RequestBody CreateTaskAttributeRequestDTO request) {
        return new ResponseEntity<>(nodeService.createNewTaskStatus(id, request), HttpStatus.CREATED);
    }

    @PostMapping("/{id}/task-types")
    public ResponseEntity<NodeResponseDTO> createNewTaskTypes(@PathVariable UUID id, @Valid @RequestBody CreateTaskAttributeRequestDTO request) {
        return new ResponseEntity<>(nodeService.createNewTaskType(id, request), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<NodeResponseDTO>> getNodeStructure() {
        return new ResponseEntity<>(nodeService.getNodeStructure(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<NodeResponseDTO> getNodeById(@PathVariable UUID id) {
        return new ResponseEntity<>(nodeService.getNodeById(id), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<NodeResponseDTO> updateNode(
            @PathVariable UUID id,
            @Valid @RequestBody UpdateNodeRequestDTO request) {
        return new ResponseEntity<>(nodeService.updateNodeById(id, request), HttpStatus.OK);
    }

    @PostMapping("/{id}")
    public ResponseEntity<NodeResponseDTO> duplicateNode(
            @PathVariable UUID id) {
        return new ResponseEntity<>(nodeService.duplicateNode(id), HttpStatus.OK);
    }

    @PutMapping("/{id}/taskStatuses/{taskStatusId}")
    public ResponseEntity<NodeResponseDTO> updateTaskStatus(@PathVariable UUID id, @PathVariable UUID taskStatusId, @Valid @RequestBody UpdateTaskAttributeRequestDTO request) {
        return new ResponseEntity<>(nodeService.updateTaskStatus(id, taskStatusId, request), HttpStatus.OK);
    }

    @PutMapping("/{id}/taskTypes/{taskTypeId}")
    public ResponseEntity<NodeResponseDTO> updateTaskType(@PathVariable UUID id, @PathVariable UUID taskTypeId, @Valid @RequestBody UpdateTaskAttributeRequestDTO request) {
        return new ResponseEntity<>(nodeService.updateTaskType(id, taskTypeId, request), HttpStatus.OK);
    }

    @DeleteMapping("/{id}/taskStatuses/{taskStatusId}")
    public ResponseEntity<Void> deleteTaskStatus(@PathVariable UUID id, @PathVariable UUID taskStatusId) {
        nodeService.deleteTaskStatus(id, taskStatusId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("/{id}/taskTypes/{taskTypeId}")
    public ResponseEntity<Void> deleteTaskType(@PathVariable UUID id, @PathVariable UUID taskTypeId) {
        nodeService.deleteTaskType(id, taskTypeId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNode(@PathVariable UUID id) {
        nodeService.deleteNodeById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
