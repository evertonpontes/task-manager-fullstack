package com.example.taskmanager.app.mappers;

import com.example.taskmanager.app.dtos.node.NodeResponseDTO;
import com.example.taskmanager.app.entities.Node;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface NodeMapper {
    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "parentNode.id", target = "parentNodeId")
    NodeResponseDTO nodeToNodeResponseDTO(Node node);

    Node cloneNode(Node node);
}
