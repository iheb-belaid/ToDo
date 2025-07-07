package com.stage.appToDo;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/tasks")
public class TaskController {

    @Autowired private TaskRepository taskRepo;
    @Autowired private CategoryRepository categoryRepo;

    /* ---------- Board ---------- */
    @GetMapping
    public String board(Model m) {
        m.addAttribute("todo",   taskRepo.findByStatus(Task.Status.TODO));
        m.addAttribute("doing",  taskRepo.findByStatus(Task.Status.IN_PROGRESS));
        m.addAttribute("done",   taskRepo.findByStatus(Task.Status.DONE));
        return "tasks/board";
    }

    /* ---------- Create / Edit ---------- */
    @GetMapping("/new")
    public String newForm(Model m) {
        m.addAttribute("task", new Task());
        loadFormLists(m);
        return "tasks/form";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable Long id, Model m) {
        m.addAttribute("task",
                taskRepo.findById(id)
                        .orElseThrow(() -> new IllegalArgumentException("Task inconnue " + id)));
        loadFormLists(m);
        return "tasks/form";
    }

    @PostMapping("/save")
    public String save(@Valid @ModelAttribute Task task, BindingResult br, Model m) {
        if (br.hasErrors()) {
            loadFormLists(m);
            return "tasks/form";
        }
        taskRepo.save(task);
        return "redirect:/tasks";
    }

    /* ---------- Delete ---------- */
    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        taskRepo.deleteById(id);
        return "redirect:/tasks";
    }

    /* ---------- AJAX Status ---------- */
    @PatchMapping("/{id}/status/{status}")
    @ResponseBody
    public void changeStatus(@PathVariable Long id,
                             @PathVariable Task.Status status) {
        taskRepo.findById(id).ifPresent(t -> {
            t.setStatus(status);
            taskRepo.save(t);
        });
    }

    /* ---------- Helpers ---------- */
    private void loadFormLists(Model m) {
        m.addAttribute("categories", categoryRepo.findAll());
        m.addAttribute("priorities", Task.Priority.values());
    }
}
