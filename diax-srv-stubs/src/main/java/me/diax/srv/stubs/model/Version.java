package me.diax.srv.stubs.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

@XmlAccessorType(XmlAccessType.PROPERTY)
@XmlRootElement(name = "version")
public class Version implements Comparable<Version>, Serializable {

    private static final long serialVersionUID = -8539067440709784602L;

    private String version;

    public Version() {
    }

    public Version(String version) {
        setVersion(version);
    }

    @Override
    public int compareTo(Version that) {
        String[] thisParts = this.version.split("\\.");
        String[] thatParts = that.version.split("\\.");
        int length = Math.max(thisParts.length, thatParts.length);
        for (int i = 0; i < length; i++) {
            int thisPart = i < thisParts.length ?
                    Integer.parseInt(thisParts[i]) : 0;
            int thatPart = i < thatParts.length ?
                    Integer.parseInt(thatParts[i]) : 0;
            if (thisPart < thatPart)
                return -1;
            if (thisPart > thatPart)
                return 1;
        }
        return 0;
    }

    @XmlElement(name = "version")
    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        if (!version.matches("[0-9]+(\\.[0-9]+)*"))
            throw new IllegalArgumentException("Invalid version format");
        this.version = version;
    }

    @Override
    public boolean equals(Object o) {
        return this == o || o != null && this.getClass() == o.getClass() && this.compareTo((Version) o) == 0;
    }

    @Override
    public String toString() {
        return version;
    }
}
