package org.example.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name = "Customer")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Customer {
    @Id
    @Column(name = "CustomerID")
    private String customerId; // Khóa chính

    @Column(name = "CustomerName", nullable = false)
    private String customerName;

    @Column(name = "Mobile", nullable = false)
    private String mobile;

    @Column(name = "Birthday", nullable = false)
    private LocalDate birthday; // Dùng LocalDate cho ngày sinh

    @Column(name = "IdentityCard", nullable = false)
    private String identityCard; // CCCD/CMND

    @Column(name = "LicenceNumber", nullable = false)
    private String licenceNumber; // Bằng lái xe

    @Column(name = "LicenceDate", nullable = false)
    private LocalDate licenceDate; // Ngày cấp bằng lái

    @Column(name = "Email", nullable = false)
    private String email;

    @Column(name = "Password", nullable = false)
    private String password;

    @OneToOne
    @JoinColumn(name = "AccountID", referencedColumnName = "AccountID", nullable = false)
    private Account account;
}
