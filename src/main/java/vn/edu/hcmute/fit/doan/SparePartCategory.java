// src/main/java/vn/edu/hcmute/fit/doan/SparePartCategory.java
package vn.edu.hcmute.fit.doan;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "spare_part_category")
public class SparePartCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false, length = 100)
    private String name;

    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SparePart> spareParts;

    public SparePartCategory() {}

    public SparePartCategory(String name) {
        this.name = name;
    }

    // --- Getters & Setters ---
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }


}