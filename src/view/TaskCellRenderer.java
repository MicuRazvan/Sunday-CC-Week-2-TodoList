package view;

import model.Task;

import javax.swing.*;
import java.awt.*;

public class TaskCellRenderer extends JPanel implements ListCellRenderer<Task> {
    private JLabel titleLabel;
    private JLabel userLabel;

    public TaskCellRenderer() {
        setLayout(new BorderLayout(5, 5));
        setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        titleLabel = new JLabel();
        userLabel = new JLabel();
        userLabel.setForeground(Color.GRAY);
        add(titleLabel, BorderLayout.CENTER);
        add(userLabel, BorderLayout.SOUTH);
    }

    @Override
    public Component getListCellRendererComponent(JList<? extends Task> list, Task task, int index,
                                                  boolean isSelected, boolean cellHasFocus) {
        titleLabel.setText(task.getTitle());
        if (task.getAssignedUser() != null) {
            userLabel.setText("Assigned to: " + task.getAssignedUser().getName());
        } else {
            userLabel.setText("Unassigned");
        }

        if (isSelected) {
            setBackground(list.getSelectionBackground());
            setForeground(list.getSelectionForeground());
        } else {
            setBackground(list.getBackground());
            setForeground(list.getForeground());
        }
        setEnabled(list.isEnabled());
        setFont(list.getFont());
        setOpaque(true);
        return this;
    }
}
