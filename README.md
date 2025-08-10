# Week 2 Project — Todo List app

## Context
I lost a bet with a friend and he challenged me that for the next 52 weeks, during weekends I need to create from scratch a new project.  

### The rules are the following:
- Each Friday night, me and him will talk about what project I need to do.  
- Mostly he will decide for me, but I’m allowed to suggest and do my own ideas if he agrees on.  
- Once the project is decided, he will tell me if I’m allowed to work Saturday **and** Sunday, or only Sunday.  
  (Surely this won’t backfire at some point by underestimating a project, right? 😅)

  
####For this week we decided on **Todo List app**, being allowed to work on it only sunday.  
---

## About the project
The main framework used is **Swing** to create the UI and its components.

The application represents each column as a `JList` component. Tasks are stored as individual `Task` objects within these lists.

When a user drags a task, Swing's `TransferHandler` exports the `Task` object from the source list and upon release, imports it into the target `JList`, effectively changing its status.

---

## Features

The To-Do application supports:

- **Full task lifecycle management**: Create new tasks, edit their title and description, or delete them entirely.
- **Interactive drag-and-drop**: Seamlessly move tasks from one list to another to update their workflow status.
- **User assignment**:
    - Create a central pool of users.
    - Assign and re-assign tasks to any user from a simple dropdown menu.
- **Dynamic list customization**:
    - Create new custom lists to fit any project workflow.
    - Rename or delete user-created lists.
    - Initial lists (`Todo`, `Finished`) are protected from changes.
