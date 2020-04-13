package com.hms_networks.americas.sc.taginfo;

/**
 * Class to represent a tag group in a similar fashion to enums in Java 1.5+.
 *
 * @author HMS Networks, MU Americas Solution Center
 * @since 1.0
 */
public class TagGroup {

  /** Integer assigned to represent tag group A. */
  private static final int GROUP_A_INT = 0;

  /** Integer assigned to represent tag group B. */
  private static final int GROUP_B_INT = 1;

  /** Integer assigned to represent tag group C. */
  private static final int GROUP_C_INT = 2;

  /** Integer assigned to represent tag group D. */
  private static final int GROUP_D_INT = 3;

  /** Public instance of {@link TagGroup} representing tag group A. */
  public static final TagGroup A = new TagGroup(GROUP_A_INT);

  /** Public instance of {@link TagGroup} representing tag group B. */
  public static final TagGroup B = new TagGroup(GROUP_B_INT);

  /** Public instance of {@link TagGroup} representing tag group C. */
  public static final TagGroup C = new TagGroup(GROUP_C_INT);

  /** Public instance of {@link TagGroup} representing tag group D. */
  public static final TagGroup D = new TagGroup(GROUP_D_INT);

  /** Instance tag group integer */
  private final int groupID;

  /**
   * Private (internal) constructor for creating an instance of {@link TagGroup} with a tag group
   * integer.
   *
   * <p>Note: Tag group integers shall be unique.
   *
   * @param groupID integer to represent tag group
   */
  private TagGroup(int groupID) {
    this.groupID = groupID;
  }
}
