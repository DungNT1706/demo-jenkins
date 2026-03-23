package org.example.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "Review")
@IdClass(ReviewId.class) // Khai báo class chứa composite key
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Review {

    @Id
    @ManyToOne
    @JoinColumn(name = "CustomerID", referencedColumnName = "CustomerID", nullable = false)
    private Customer customer;

    @Id
    @ManyToOne
    @JoinColumn(name = "CarID", referencedColumnName = "CarID", nullable = false)
    private Car car;

    @Column(name = "ReviewStar", nullable = false)
    private Integer reviewStar;

    // Sử dụng columnDefinition = "NVARCHAR(MAX)" để SQL Server hỗ trợ gõ tiếng Việt có dấu
    @Column(name = "Comment", nullable = false, columnDefinition = "NVARCHAR(MAX)")
    private String comment;
}