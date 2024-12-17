import java.awt.*;
import javax.swing.*;
import java.util.Calendar;

public class Main {

    public static void main(String[] args) {
        JFrame frame = new JFrame("Analog Clock");
        ClockPanel clockPanel = new ClockPanel();

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 400);
        frame.setLayout(new BorderLayout());

        frame.add(clockPanel, BorderLayout.CENTER);

        JPanel controlPanel = new JPanel();
        JButton addHourButton = new JButton("+1 Hour");
        JButton subtractHourButton = new JButton("-1 Hour");
        JButton addMinuteButton = new JButton("+1 Minute");
        JButton subtractMinuteButton = new JButton("-1 Minute");

        controlPanel.add(addHourButton);
        controlPanel.add(subtractHourButton);
        controlPanel.add(addMinuteButton);
        controlPanel.add(subtractMinuteButton);
        frame.add(controlPanel, BorderLayout.SOUTH);

        addHourButton.addActionListener(e -> clockPanel.adjustTime(1, 0));
        subtractHourButton.addActionListener(e -> clockPanel.adjustTime(-1, 0));
        addMinuteButton.addActionListener(e -> clockPanel.adjustTime(0, 1));
        subtractMinuteButton.addActionListener(e -> clockPanel.adjustTime(0, -1));

        frame.setVisible(true);
    }
}

class ClockPanel extends JPanel {
    private Calendar calendar;

    public ClockPanel() {
        calendar = Calendar.getInstance();
        Timer timer = new Timer(1000, e -> {
            calendar.add(Calendar.SECOND, 1);
            repaint();
        });
        timer.start();
    }

    public void adjustTime(int hourDelta, int minuteDelta) {
        calendar.add(Calendar.HOUR, hourDelta);
        calendar.add(Calendar.MINUTE, minuteDelta);
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        int centerX = getWidth() / 2;
        int centerY = getHeight() / 2;
        int radius = Math.min(centerX, centerY) - 10;

        g2.setColor(Color.BLACK);
        g2.drawOval(centerX - radius, centerY - radius, 2 * radius, 2 * radius);

        for (int i = 0; i < 12; i++) {
            double angle = Math.toRadians(i * 30);
            int x1 = centerX + (int) (radius * 0.9 * Math.sin(angle));
            int y1 = centerY - (int) (radius * 0.9 * Math.cos(angle));
            int x2 = centerX + (int) (radius * Math.sin(angle));
            int y2 = centerY - (int) (radius * Math.cos(angle));
            g2.drawLine(x1, y1, x2, y2);
        }

        int hour = calendar.get(Calendar.HOUR);
        int minute = calendar.get(Calendar.MINUTE);
        int second = calendar.get(Calendar.SECOND);

        drawHand(g2, centerX, centerY, radius * 0.6, hour * 30 + minute * 0.5, 6);
        drawHand(g2, centerX, centerY, radius * 0.8, minute * 6, 4);
        drawHand(g2, centerX, centerY, radius * 0.9, second * 6, 2, Color.RED);
    }

    private void drawHand(Graphics2D g2, int x, int y, double length, double angleDegrees, int width) {
        drawHand(g2, x, y, length, angleDegrees, width, Color.BLACK);
    }

    private void drawHand(Graphics2D g2, int x, int y, double length, double angleDegrees, int width, Color color) {
        double angleRadians = Math.toRadians(angleDegrees - 90);
        int xEnd = x + (int) (length * Math.cos(angleRadians));
        int yEnd = y + (int) (length * Math.sin(angleRadians));

        g2.setColor(color);
        g2.setStroke(new BasicStroke(width));
        g2.drawLine(x, y, xEnd, yEnd);
    }
}
