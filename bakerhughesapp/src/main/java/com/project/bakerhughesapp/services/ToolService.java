package com.project.bakerhughesapp.services;

import com.project.bakerhughesapp.dtos.ToolDTO;
import com.project.bakerhughesapp.models.Tool;
import com.project.bakerhughesapp.repositories.ToolRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor // khong can tao constructor trong moi method
public class ToolService implements IToolService{

    private final ToolRepository toolRepository;

    // create new tool
    @Override
    public Tool createTool(ToolDTO toolDTO) {
        Tool newTool = Tool.builder()
                .sensorOffset(toolDTO.getSensorOffset())
                .defaultBUR(toolDTO.getDefaultBur())
                .build();

        return toolRepository.save(newTool);
    }

    // get Tool by ID
    @Override
    public Optional<Tool> getToolByID(long id) {
        return Optional.ofNullable(toolRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Tool not found")));
    }

    // update tool information
    @Override
    public Tool updateTool(long toolId, ToolDTO toolDTO) {
        Tool existingTool = getToolByID(toolId)
                .orElseThrow(() -> new RuntimeException("Tool not found"));

        // Update existing Tool with new tool information
        existingTool.setSensorOffset(toolDTO.getSensorOffset());
        existingTool.setDefaultBUR(toolDTO.getDefaultBur());

        return toolRepository.save(existingTool);
    }


}
