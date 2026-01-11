package ma.emsi.entities.modules.users;

import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import ma.emsi.entities.base.BaseEntity;
import ma.emsi.entities.modules.enums.RoleType;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Role extends BaseEntity {

    private String libelle;
    private RoleType type;
    private List<String> privileges = new ArrayList<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Role)) return false;
        Role that = (Role) o;
        return id != null && id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "Role{id=" + id + ", libelle='" + libelle + "', type=" + type + "}";
    }
}
