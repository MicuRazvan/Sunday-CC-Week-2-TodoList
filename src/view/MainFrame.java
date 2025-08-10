package view;

import model.Task;
import model.User;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

public class MainFrame extends JFrame {

    private final List<User> users = new ArrayList<>();
    private final List<JList<Task>> taskLists = new ArrayList<>();
    private final List<String> listTitles = new ArrayList<>();
    private final JPanel mainPanel = new JPanel();

    public MainFrame() {
        setTitle("To-Do Application");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 600);
        setLocationRelativeTo(null);

        users.add(new User("Panda"));
        users.add(new User("Grizzly"));

        mainPanel.setLayout(new GridLayout(1, 0, 10, 10));
        add(new JScrollPane(mainPanel));

        createNewList("Todo", false);
        createNewList("Finished", false);
        createNewList("Testing", true);

        setupMenuBar();
        setVisible(true);
    }

    private void setupMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("File");
        JMenuItem newTaskItem = new JMenuItem("New Task");
        JMenuItem newList = new JMenuItem("New List");
        JMenuItem newUser = new JMenuItem("Add User");

        newTaskItem.addActionListener(e -> createNewTask());
        newList.addActionListener(e -> {
            String name = JOptionPane.showInputDialog(this, "Enter list name:", "New List", JOptionPane.PLAIN_MESSAGE);
            if (name != null && !name.trim().isEmpty()) {
                createNewList(name, true);
            }
        });
        newUser.addActionListener(e -> {
            String name = JOptionPane.showInputDialog(this, "Enter user name:", "New User", JOptionPane.PLAIN_MESSAGE);
            if (name != null && !name.trim().isEmpty()) {
                users.add(new User(name));
                JOptionPane.showMessageDialog(this, "User '" + name + "' added.");
            }
        });

        fileMenu.add(newTaskItem);
        fileMenu.add(newList);
        fileMenu.add(newUser);
        menuBar.add(fileMenu);
        setJMenuBar(menuBar);
    }

    private void createNewList(String title, boolean isDeletable) {
        DefaultListModel<Task> listModel = new DefaultListModel<>();
        JList<Task> taskList = new JList<>(listModel);
        taskList.setCellRenderer(new TaskCellRenderer());
        taskList.setDragEnabled(true);
        taskList.setDropMode(DropMode.INSERT);
        taskList.setTransferHandler(new TaskTransferHandler());
        taskList.addMouseListener(createTaskMouseListener(taskList));

        taskLists.add(taskList);
        listTitles.add(title);

        JPanel listPanel = new JPanel(new BorderLayout());
        JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        headerPanel.add(new JLabel(title));

        if (isDeletable) {
            JButton deleteButton = new JButton("X");
            deleteButton.setMargin(new Insets(0, 2, 0, 2));
            deleteButton.addActionListener(e -> deleteList(listPanel, taskList));
            headerPanel.add(deleteButton);
        }

        JButton renameButton = new JButton("Rename");
        renameButton.addActionListener(e -> renameList(listPanel, title));
        if(title.equals("Todo") || title.equals("Finished")){
            renameButton.setEnabled(false);
        }
        headerPanel.add(renameButton);

        listPanel.add(headerPanel, BorderLayout.NORTH);
        listPanel.add(new JScrollPane(taskList), BorderLayout.CENTER);
        listPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));

        mainPanel.add(listPanel);
        mainPanel.revalidate();
        mainPanel.repaint();
    }

    private void renameList(JPanel listPanel, String oldTitle){
        String newTitle = JOptionPane.showInputDialog(this, "Enter new list name:", "Rename List", JOptionPane.PLAIN_MESSAGE);
        if (newTitle != null && !newTitle.trim().isEmpty()) {
            int index = mainPanel.getComponentZOrder(listPanel);
            listTitles.set(index, newTitle);

            JPanel headerPanel = (JPanel)listPanel.getComponent(0);
            JLabel titleLabel = (JLabel)headerPanel.getComponent(0);
            titleLabel.setText(newTitle);

            mainPanel.revalidate();
            mainPanel.repaint();
        }
    }

    private void deleteList(JPanel listPanel, JList<Task> taskList) {
        int response = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this list?", "Confirm Deletion", JOptionPane.YES_NO_OPTION);
        if (response == JOptionPane.YES_OPTION) {
            mainPanel.remove(listPanel);
            taskLists.remove(taskList);
            mainPanel.revalidate();
            mainPanel.repaint();
        }
    }

    private void createNewTask() {
        if (taskLists.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Create a list first!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        String title = JOptionPane.showInputDialog(this, "Enter task title:", "New Task", JOptionPane.PLAIN_MESSAGE);
        if (title != null && !title.trim().isEmpty()) {
            Task newTask = new Task(title);
            ((DefaultListModel<Task>) taskLists.get(0).getModel()).addElement(newTask);
        }
    }

    private MouseAdapter createTaskMouseListener(JList<Task> list) {
        return new MouseAdapter() {
            public void mouseClicked(MouseEvent evt) {
                if (evt.getClickCount() == 2) { // Double-click
                    int index = list.locationToIndex(evt.getPoint());
                    if (index >= 0) {
                        Task task = list.getModel().getElementAt(index);
                        showEditTaskDialog(task, list);
                    }
                }
            }
        };
    }

    private void showEditTaskDialog(Task task, JList<Task> list) {
        // UI components for the dialog's content
        JTextField titleField = new JTextField(task.getTitle());
        JTextArea descArea = new JTextArea(task.getDescription(), 5, 20);
        JComboBox<User> userComboBox = new JComboBox<>(new Vector<>(users));
        userComboBox.setSelectedItem(task.getAssignedUser());

        // build the panel that will be the body of the dialog
        JPanel panel = new JPanel(new GridLayout(0, 1, 5, 5)); // Added some gaps for spacing
        panel.add(new JLabel("Title:"));
        panel.add(titleField);
        panel.add(new JLabel("Description:"));
        panel.add(new JScrollPane(descArea));
        panel.add(new JLabel("Assign to:"));
        panel.add(userComboBox);

        // texts for the buttons
        String[] options = {"Save Changes", "Delete Task", "Cancel"};

        // showOptionDialog is designed for custom button layouts.
        int result = JOptionPane.showOptionDialog(this, panel, "Edit Task", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE, null, options, options[0]);

        switch (result) {
            case 0: // user clicked "Save Changes" (index 0)
                task.setTitle(titleField.getText());
                task.setDescription(descArea.getText());
                task.setAssignedUser((User) userComboBox.getSelectedItem());
                list.repaint(); // Repaint the list to reflect the changes
                break;

            case 1: // user clicked "Delete Task" (index 1)
                // ask for confirmation before deleting
                int confirm = JOptionPane.showConfirmDialog(
                        this,
                        "Are you sure you want to delete this task?",
                        "Confirm Deletion",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.WARNING_MESSAGE
                );
                if (confirm == JOptionPane.YES_OPTION) {
                    ((DefaultListModel<Task>) list.getModel()).removeElement(task);
                }
                break;
            case 2: // user clicked "Cancel" (index 2)
            default: // or closed the dialog window
                break;
        }
    }

    class TaskTransferHandler extends TransferHandler {
        public final DataFlavor TASK_FLAVOR = new DataFlavor(Task.class, "Task Object");
        private int sourceIndex;
        private JList<Task> sourceList;

        @Override
        public int getSourceActions(JComponent c) {
            return MOVE;
        }

        @Override
        protected Transferable createTransferable(JComponent c) {
            sourceList = (JList<Task>) c;
            sourceIndex = sourceList.getSelectedIndex();
            Task task = sourceList.getSelectedValue();
            return new TaskTransferable(task);
        }

        @Override
        protected void exportDone(JComponent source, Transferable data, int action) {
            if (action == MOVE) {
                ((DefaultListModel<Task>) sourceList.getModel()).remove(sourceIndex);
            }
        }

        @Override
        public boolean canImport(TransferSupport support) {
            return support.isDataFlavorSupported(TASK_FLAVOR);
        }

        @Override
        public boolean importData(TransferSupport support) {
            if (!canImport(support)) {
                return false;
            }
            try {
                Task task = (Task) support.getTransferable().getTransferData(TASK_FLAVOR);
                JList.DropLocation dl = (JList.DropLocation) support.getDropLocation();
                JList<Task> targetList = (JList<Task>) support.getComponent();
                DefaultListModel<Task> model = (DefaultListModel<Task>) targetList.getModel();
                int dropIndex = dl.getIndex();
                model.insertElementAt(task, dropIndex);
                return true;
            } catch (UnsupportedFlavorException | IOException e) {
                e.printStackTrace();
            }
            return false;
        }

        private class TaskTransferable implements Transferable {
            private final Task task;

            public TaskTransferable(Task task) {
                this.task = task;
            }

            @Override
            public DataFlavor[] getTransferDataFlavors() {
                return new DataFlavor[]{TASK_FLAVOR};
            }

            @Override
            public boolean isDataFlavorSupported(DataFlavor flavor) {
                return flavor.equals(TASK_FLAVOR);
            }

            @Override
            public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException {
                if (flavor.equals(TASK_FLAVOR)) {
                    return task;
                }
                throw new UnsupportedFlavorException(flavor);
            }
        }
    }
}
