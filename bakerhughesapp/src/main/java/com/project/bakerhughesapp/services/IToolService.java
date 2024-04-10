package com.project.bakerhughesapp.services;

import com.project.bakerhughesapp.dtos.ToolDTO;
import com.project.bakerhughesapp.models.Tool;

import java.util.Optional;

public interface IToolService {
    //create new tool
    Tool createTool(ToolDTO toolDTO);

    // update Existing tool properties
    Tool updateTool(long toolId, ToolDTO toolDTO);

    // get Tool by ID
    Optional<Tool> getToolByID(long id);
}
