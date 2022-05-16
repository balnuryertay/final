package narxoz.balnur.LibraryApp.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "books")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Books {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "author")
    private String author;

    @Column(name = "price")
    private int price;

    @Column(name = "reviews")
    private float reviews;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;


    @ManyToOne()
    private BookCategory categories;

}
