package team_f.database_wrapper.entities;

import javax.persistence.*;
import java.util.Collection;

@Entity
@Table(name = "StringInstrumentation", schema = "sem4_team2", catalog = "")
public class StringInstrumentationEntity {
    private int stringInstrumentationId;
    private int violin1;
    private int violin2;
    private int viola;
    private int violincello;
    private int doublebass;
    private Collection<InstrumentationEntity> instrumentationsByStringInstrumentationId;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "stringInstrumentationID", nullable = false)
    public int getStringInstrumentationId() {
        return stringInstrumentationId;
    }

    public void setStringInstrumentationId(int stringInstrumentationId) {
        this.stringInstrumentationId = stringInstrumentationId;
    }

    @Basic
    @Column(name = "violin1", nullable = false)
    public int getViolin1() {
        return violin1;
    }

    public void setViolin1(int violin1) {
        this.violin1 = violin1;
    }

    @Basic
    @Column(name = "violin2", nullable = false)
    public int getViolin2() {
        return violin2;
    }

    public void setViolin2(int violin2) {
        this.violin2 = violin2;
    }

    @Basic
    @Column(name = "viola", nullable = false)
    public int getViola() {
        return viola;
    }

    public void setViola(int viola) {
        this.viola = viola;
    }

    @Basic
    @Column(name = "violincello", nullable = false)
    public int getViolincello() {
        return violincello;
    }

    public void setViolincello(int violincello) {
        this.violincello = violincello;
    }

    @Basic
    @Column(name = "doublebass", nullable = false)
    public int getDoublebass() {
        return doublebass;
    }

    public void setDoublebass(int doublebass) {
        this.doublebass = doublebass;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        StringInstrumentationEntity that = (StringInstrumentationEntity) o;

        if (stringInstrumentationId != that.stringInstrumentationId) return false;
        if (violin1 != that.violin1) return false;
        if (violin2 != that.violin2) return false;
        if (viola != that.viola) return false;
        if (violincello != that.violincello) return false;
        if (doublebass != that.doublebass) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = stringInstrumentationId;
        result = 31 * result + violin1;
        result = 31 * result + violin2;
        result = 31 * result + viola;
        result = 31 * result + violincello;
        result = 31 * result + doublebass;
        return result;
    }

    @OneToMany(mappedBy = "stringInstrumentationByStringInstrumentation")
    public Collection<InstrumentationEntity> getInstrumentationsByStringInstrumentationId() {
        return instrumentationsByStringInstrumentationId;
    }

    public void setInstrumentationsByStringInstrumentationId(Collection<InstrumentationEntity> instrumentationsByStringInstrumentationId) {
        this.instrumentationsByStringInstrumentationId = instrumentationsByStringInstrumentationId;
    }
}
