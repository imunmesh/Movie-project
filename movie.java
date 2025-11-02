import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class Movie extends JFrame {

    // JDBC variables
    Connection con;
    PreparedStatement pst;

    // GUI components
    JTextField txtCustomerID, txtMovieID, txtSeats;

    public MovieBookingSystem() {
        setTitle("🎟 Online Movie Ticket Booking System");
        setSize(400, 250);
        setLayout(new GridLayout(4, 2, 10, 10));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Connect to MySQL
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/Az",
                    "root",  // 🔁 your MySQL username
                    "abc123"       // 🔁 your MySQL password (if any)
            );
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Database Connection Failed: " + e.getMessage());
        }

        // GUI labels and fields
        add(new JLabel("Customer ID:"));
        txtCustomerID = new JTextField();
        add(txtCustomerID);

        add(new JLabel("Movie ID:"));
        txtMovieID = new JTextField();
        add(txtMovieID);

        add(new JLabel("Seats:"));
        txtSeats = new JTextField();
        add(txtSeats);

        JButton btnBook = new JButton("Book Ticket");
        add(btnBook);
        JButton btnExit = new JButton("Exit");
        add(btnExit);

        // Button actions
        btnBook.addActionListener(e -> bookTicket());
        btnExit.addActionListener(e -> System.exit(0));

        setVisible(true);
    }

    void bookTicket() {
        String cid = txtCustomerID.getText();
        String mid = txtMovieID.getText();
        String seats = txtSeats.getText();

        if (cid.isEmpty() || mid.isEmpty() || seats.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill all fields!");
            return;
        }

        try {
            String sql = "INSERT INTO Booking (Seats, Customer_ID, Movie_ID) VALUES (?, ?, ?)";
            pst = con.prepareStatement(sql);
            pst.setInt(1, Integer.parseInt(seats));
            pst.setInt(2, Integer.parseInt(cid));
            pst.setInt(3, Integer.parseInt(mid));
            pst.executeUpdate();

            JOptionPane.showMessageDialog(this, "✅ Ticket Booked Successfully!");
        } catch (SQLIntegrityConstraintViolationException e) {
            JOptionPane.showMessageDialog(this, "⚠ Invalid Customer or Movie ID!");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(MovieBookingSystem::new);
    }
}
