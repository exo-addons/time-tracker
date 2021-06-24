package org.exoplatform.timetracker.dto;

import java.io.Serializable;

import lombok.Data;

@Data
public class TimeTrackerSetting implements Serializable, Cloneable {

  private static final long serialVersionUID          = 4954396895412734575L;

  private SubActivityCode   defaultFeatureSubActivity = null;

  private String            usersSpace                = null;

  public TimeTrackerSetting clone() { // NOSONAR
    try {
      return (TimeTrackerSetting) super.clone();
    } catch (CloneNotSupportedException e) {
      throw new IllegalStateException("Error while cloning object: " + this, e);
    }
  }

}
