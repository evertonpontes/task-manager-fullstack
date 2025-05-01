package com.everton.taskmanager.controllers;

import com.everton.taskmanager.dtos.task.*;
import com.everton.taskmanager.services.LoggedTimeService;
import com.everton.taskmanager.services.ScheduledWorkService;
import com.everton.taskmanager.services.SubTaskService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/organizations/{organizationId}/tasks/{taskId}")
public class TaskAttributesController {
    @Autowired
    private SubTaskService subTaskService;

    @Autowired
    private ScheduledWorkService scheduledWorkService;

    @Autowired
    private LoggedTimeService loggedTimeService;

    @PostMapping("subtasks")
    public ResponseEntity<SubTaskResponseDTO> createSubTask(@PathVariable("taskId") String taskId,
                                                            @RequestBody @Valid SaveSubTaskDTO subTaskDTO) {
        return new ResponseEntity<>(subTaskService.createSubTask(taskId, subTaskDTO), HttpStatus.CREATED);
    }

    @GetMapping("subtasks")
    public ResponseEntity<List<SubTaskResponseDTO>> getSubTasks(@PathVariable("taskId") String taskId) {
        return new ResponseEntity<>(subTaskService.findAllSubTasksInTask(taskId), HttpStatus.OK);
    }

    @PutMapping("subtasks/{subTaskId}")
    public ResponseEntity<SubTaskResponseDTO> updateSubTask(@PathVariable("subTaskId") String subTaskId,
                                                            @RequestBody @Valid SaveSubTaskDTO subTaskDTO) {
        return new ResponseEntity<>(subTaskService.updateSubTask(subTaskId, subTaskDTO), HttpStatus.OK);
    }

    @DeleteMapping("subtasks/{subTaskId}")
    public ResponseEntity<Void> deleteSubTasks(@PathVariable("subTaskId") String subTaskId) {
        subTaskService.deleteSubTask(subTaskId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("scheduled_works")
    public ResponseEntity<ScheduledWorkResponseDTO> createScheduledWork(@PathVariable("taskId") String taskId,
                                                                        @RequestBody @Valid SaveScheduledWorksDTO scheduledWorksDTO) {
        return new ResponseEntity<>(scheduledWorkService.createScheduledWork(taskId, scheduledWorksDTO), HttpStatus.CREATED);
    }

    @GetMapping("scheduled_works")
    public ResponseEntity<List<ScheduledWorkResponseDTO>> getScheduledWork(@PathVariable("taskId") String taskId) {
        return new ResponseEntity<>(scheduledWorkService.findAllScheduledWorksInTask(taskId), HttpStatus.OK);
    }

    @PutMapping("scheduled_works/{scheduledWorkId}")
    public ResponseEntity<ScheduledWorkResponseDTO> updateScheduledWork(@PathVariable("scheduledWorkId") String scheduledWorkId,
                                                            @RequestBody @Valid SaveScheduledWorksDTO scheduledWorksDTO) {
        return new ResponseEntity<>(scheduledWorkService.updateScheduled(scheduledWorkId, scheduledWorksDTO), HttpStatus.OK);
    }

    @DeleteMapping("scheduled_works/{scheduledWorkId}")
    public ResponseEntity<Void> deleteScheduledWork(@PathVariable("scheduledWorkId") String scheduledWorkId) {
        scheduledWorkService.deleteScheduledWork(scheduledWorkId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("logged_times")
    public ResponseEntity<LoggedTimeResponseDTO> createLoggedTime(@PathVariable("taskId") String taskId,
                                                                  @RequestBody @Valid SaveLoggedTimeDTO loggedTimeDTO) {
        return new ResponseEntity<>(loggedTimeService.createLoggedTime(taskId, loggedTimeDTO), HttpStatus.CREATED);
    }

    @GetMapping("logged_times")
    public ResponseEntity<List<LoggedTimeResponseDTO>> getLoggedTime(@PathVariable("taskId") String taskId) {
        return new ResponseEntity<>(loggedTimeService.findAllLoggedTimesInTask(taskId), HttpStatus.OK);
    }

    @PutMapping("logged_times/{loggedTimeId}")
    public ResponseEntity<LoggedTimeResponseDTO> updateLoggedTime(@PathVariable("loggedTimeId") String loggedTimeId,
                                                                  @RequestBody @Valid SaveLoggedTimeDTO loggedTimeDTO) {
        return new ResponseEntity<>(loggedTimeService.updateLoggedTime(loggedTimeId, loggedTimeDTO), HttpStatus.CREATED);
    }

    @DeleteMapping("logged_times/{loggedTimeId}")
    public ResponseEntity<Void> deleteLoggedTime(@PathVariable("loggedTimeId") String loggedTimeId) {
        loggedTimeService.deleteLoggedTime(loggedTimeId);
        return ResponseEntity.noContent().build();
    }
}
