package com.hms_networks.americas.sc.taginfo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Class to hold information about a tag and its configuration.
 *
 * @author HMS Networks, MU Americas Solution Center
 * @since 1.0
 */
public class TagInfo {

  /** Tag type */
  private final TagType type;

  /** Boolean if historical logging enabled */
  private final boolean historicalLogEnabled;

  /** Boolean if real time logging enabled */
  private final boolean realTimeLogEnabled;

  /** Tag ID */
  private final int id;

  /** Tag name */
  private final String name;

  /** List of tag groups */
  private final ArrayList tagGroups;

  /** Integer tags can be used to represent string values in a string mapping */
  private final String[] enumeratedStringValueMapping;

  /**
   * Constructor for tag class with group booleans.
   *
   * @param id tag ID
   * @param name tag name
   * @param historicalLogEnabled boolean if historical logging enabled
   * @param isInGroupA boolean if tag in group A
   * @param isInGroupB boolean if tag in group B
   * @param isInGroupC boolean if tag in group C
   * @param isInGroupD boolean if tag in group D
   * @param type tag type
   */
  public TagInfo(
      int id,
      String name,
      boolean historicalLogEnabled,
      boolean realTimeLogEnabled,
      boolean isInGroupA,
      boolean isInGroupB,
      boolean isInGroupC,
      boolean isInGroupD,
      TagType type) {
    this(
        id,
        name,
        historicalLogEnabled,
        realTimeLogEnabled,
        isInGroupA,
        isInGroupB,
        isInGroupC,
        isInGroupD,
        type,
        (String[]) null);
  }

  /**
   * Constructor for tag class with supplied tag groups.
   *
   * @param id tag ID
   * @param name tag name
   * @param historicalLogEnabled boolean if historical logging enabled
   * @param tagGroups list of tag groups
   * @param type tag type
   */
  public TagInfo(
      int id,
      String name,
      boolean historicalLogEnabled,
      boolean realTimeLogEnabled,
      ArrayList tagGroups,
      TagType type) {
    this(id, name, historicalLogEnabled, realTimeLogEnabled, tagGroups, type, (String[]) null);
  }

  /**
   * Constructor for tag class with supplied tag groups and enumerated int to string value mappings.
   *
   * @param id tag ID
   * @param name tag name
   * @param historicalLogEnabled boolean if historical logging enabled
   * @param tagGroups list of tag groups
   * @param type tag type
   * @param enumeratedStringValueMapping array of strings where the integer value of the tag
   *     represents the string array index
   */
  public TagInfo(
      int id,
      String name,
      boolean historicalLogEnabled,
      boolean realTimeLogEnabled,
      ArrayList tagGroups,
      TagType type,
      String[] enumeratedStringValueMapping) {
    this.enumeratedStringValueMapping = enumeratedStringValueMapping;
    this.type = type;
    this.historicalLogEnabled = historicalLogEnabled;
    this.realTimeLogEnabled = realTimeLogEnabled;
    this.id = id;
    this.name = name;
    this.tagGroups = tagGroups;
  }

  /**
   * Constructor for tag class with group booleans and with enumerated string value mappings.
   *
   * @param id tag ID
   * @param name tag name
   * @param historicalLogEnabled boolean if historical logging enabled
   * @param isInGroupA boolean if tag in group A
   * @param isInGroupB boolean if tag in group B
   * @param isInGroupC boolean if tag in group C
   * @param isInGroupD boolean if tag in group D
   * @param type tag type
   * @param enumeratedStringValueMapping array of strings where the integer value of the tag
   *     represents the string array index
   */
  public TagInfo(
      int id,
      String name,
      boolean historicalLogEnabled,
      boolean realTimeLogEnabled,
      boolean isInGroupA,
      boolean isInGroupB,
      boolean isInGroupC,
      boolean isInGroupD,
      TagType type,
      String[] enumeratedStringValueMapping) {
    this.enumeratedStringValueMapping = enumeratedStringValueMapping;
    this.type = type;
    this.historicalLogEnabled = historicalLogEnabled;
    this.realTimeLogEnabled = realTimeLogEnabled;
    this.id = id;
    this.name = name;
    this.tagGroups = new ArrayList();

    if (isInGroupA) {
      this.tagGroups.add(TagGroup.A);
    }
    if (isInGroupB) {
      this.tagGroups.add(TagGroup.B);
    }
    if (isInGroupC) {
      this.tagGroups.add(TagGroup.C);
    }
    if (isInGroupD) {
      this.tagGroups.add(TagGroup.D);
    }
  }

  /**
   * Get the enumerated int to String value mapping for this tag.
   *
   * @return the enumerated int to String value mapping for this tag.
   */
  public String[] getEnumeratedStringValueMapping() {
    return enumeratedStringValueMapping;
  }

  /**
   * Get the tag ID
   *
   * @return tag ID
   */
  public int getId() {
    return id;
  }

  /**
   * Get the tag type
   *
   * @return tag type
   */
  public TagType getType() {
    return type;
  }

  /**
   * Get the tag name
   *
   * @return tag name
   */
  public String getName() {
    return name;
  }

  /**
   * Get if the tag historical logging is enabled
   *
   * @return true if historical logging enabled
   */
  public boolean isHistoricalLogEnabled() {
    return historicalLogEnabled;
  }

  /**
   * Get if the tag real time logging is enabled
   *
   * @return true if real time logging enabled
   */
  public boolean isRealTimeLogEnabled() {
    return realTimeLogEnabled;
  }

  /**
   * Get the tag's groups as a list of {@link TagGroup}s.
   *
   * @return tag groups list
   */
  public List getTagGroups() {
    return Collections.unmodifiableList(tagGroups);
  }
}
