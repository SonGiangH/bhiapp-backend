package com.project.bakerhughesapp.controllers;
import com.project.bakerhughesapp.dtos.ToolDTO;
import com.project.bakerhughesapp.models.ResponseObject;
import com.project.bakerhughesapp.models.Tool;
import com.project.bakerhughesapp.services.IToolService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/tool")
@RequiredArgsConstructor
public class ToolController {
    private final IToolService toolService;

    // create new tool
    @CrossOrigin(origins = "*")
    @PostMapping("")
    public ResponseEntity<ResponseObject> createTool(
            @Valid @RequestBody ToolDTO toolDTO,
            BindingResult result) {
        if (result.hasErrors()) {
            List<String> errorMessages = result.getFieldErrors()
                    .stream()
                    .map(FieldError::getDefaultMessage)
                    .toList();

            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseObject("Failed", "Unable to create new tool", errorMessages))
                    ;
        }

        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("OK", "Create new tool successfully", toolService.createTool(toolDTO))
        );
    }

    // get tool by ID
    @CrossOrigin(origins = "*")
    @GetMapping("/{id}")
    public ResponseEntity<ResponseObject> getToolByID(@PathVariable long id) {
        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("ok", "Query survey successfully",
                        toolService.getToolByID(id))
        );
    }

    // update tool by ID
    @CrossOrigin(origins = "*")
    @PutMapping("/{id}")
    public ResponseEntity<ResponseObject> updateTool(@RequestBody ToolDTO toolDTO,
                                                       @PathVariable Long id) {

        Tool updatedTool = toolService.updateTool(id, toolDTO);

        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("ok", "Update sensor offset and motor output successfully !", updatedTool)
        );
    }
}

