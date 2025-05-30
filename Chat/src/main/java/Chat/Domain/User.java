package Chat.Domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;

import lombok.Setter;

@Entity
@Table(name = "users")
@Getter
@Setter
@AllArgsConstructor
public class User {
    @JsonCreator
    public User(@JsonProperty("id") Long id) {
        this.id = id;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private Status status;

    private String password;

    @Column(unique = true, nullable = false)
    private String username;

    public String getUsername() {
        return this.username;
    }
    public void setStatus(Status status) {
        this.status = status;
    }
    public Status getStatus()
    {
        return this.status;
    }
    public Long getId(){
        return this.id;
    }
    public User(){
    }
}
