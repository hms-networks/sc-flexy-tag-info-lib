package com.hms_networks.americas.sc.taginfo;

import com.ewon.ewonitf.Exporter;
import com.ewon.ewonitf.IOManager;
import com.hms_networks.americas.sc.string.QuoteSafeStringTokenizer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Class that allows the retrieval of tag information of an Ewon Flexy by generating an export block
 * descriptor call and parsing the response to store as a list of {@link TagInfo} objects.
 *
 * @author HMS Networks, MU Americas Solution Center
 * @since 1.0
 */
public class TagInfoManager {

  /**
   * Tag information list. Contents are generated/populated by a call to {@link #refreshTagList()}.
   */
  private static TagInfo[] tagInfoList = null;

  /**
   * The current index for inserting tag information objects into the tag information list. This
   * index also doubles also a tracker for the tag information list actual size.
   */
  private static int tagInfoListInsertIndex = 0;

  /**
   * The lowest tag ID seen during the previous call to {@link #refreshTagList()}.
   */
  private static int lowestTagIdSeen = TagConstants.UNINIT_INT_VAL;

  /**
   * The highest tag ID seen during the previous call to {@link #refreshTagList()}.
   */
  private static int highestTagIdSeen = TagConstants.UNINIT_INT_VAL;

  /**
   * Populate the tag information list by using an Ewon Export Block Descriptor and parsing the
   * response.
   *
   * @throws IOException if EDB fails
   */
  public synchronized static void refreshTagList() throws IOException {
    // Create tagInfoList of size = number of Flexy tags
    tagInfoList = new TagInfo[IOManager.getNbTags()];
    tagInfoListInsertIndex = 0;

    /*
     * Create exporter
     *
     * dtTL = data type: tag list
     * ftT = file type: text
     */
    Exporter exporter = new Exporter("$dtTL$ftT");

    // Create flag to track reading header
    boolean isHeaderReceived = false;

    // Create array with max capacity of 1000
    final int maxBytesPerLine = 1000;
    int receivedBytesInsertIndex = 0;
    byte[] receivedBytes = new byte[maxBytesPerLine];

    // Loop through bytes in exporter result
    while (exporter.available() != 0) {

      // Read next byte from exporter
      int currentByteRead = exporter.read();

      // If received new line, process line (disregard if header)
      if (currentByteRead == TagConstants.TAG_EBD_NEW_LINE) {

        // Process line if not header, otherwise change header read flag
        if (isHeaderReceived) {
          processTagListEBDLine(receivedBytes);
        } else {
          isHeaderReceived = true;
        }

        // Reached end of line. Reset byte received array
        receivedBytes = new byte[maxBytesPerLine];
      }

      /*
       * Add received byte to array (if not new line,
       * carriage return or end of stream)
       */
      if (currentByteRead != TagConstants.TAG_EBD_END_OF_STREAM
          && currentByteRead != TagConstants.TAG_EBD_CARRIAGE_RETURN
          && currentByteRead != TagConstants.TAG_EBD_NEW_LINE) {
        receivedBytes[receivedBytesInsertIndex] = (byte) currentByteRead;
        receivedBytesInsertIndex++;
      }
    }

    // Flag for garbage collection
    System.gc();
  }

  /**
   * Parse the specified line from the tag information EBD data generated in {@link
   * #refreshTagList()}. Add the parse tag information to the tag information list.
   */
  private synchronized static void processTagListEBDLine(byte[] line) {
    /*
     * Token indices
     * index 0 - tag ID
     * index 1 - name
     * index 8 - historical logging enabled
     * index 25 - in tag group A
     * index 26 - in tag group B
     * index 27 - in tag group C
     * index 28 - in tag group D
     * index 55 - tag type
     * index 61 - end of line
     */
    final int indexTagId = 0;
    final int indexName = 1;
    final int indexHistoricalLogging = 8;
    final int indexGroupA = 25;
    final int indexGroupB = 26;
    final int indexGroupC = 27;
    final int indexGroupD = 28;
    final int indexType = 55;
    final int indexEnd = indexType + 1;

    // Tag information
    String tagName = "";
    int tagId = TagConstants.UNINIT_INT_VAL;
    int tagType = TagConstants.UNINIT_INT_VAL;
    boolean tagInGroupA = false;
    boolean tagInGroupB = false;
    boolean tagInGroupC = false;
    boolean tagInGroupD = false;
    boolean tagHistoricalLoggingEnabled = false;

    // Convert line to string
    String lineFromBytes = new String(line);

    // Tokenize line
    final boolean returnDelimiters = false;
    final String delimiter = ";";
    QuoteSafeStringTokenizer quoteSafeStringTokenizer =
        new QuoteSafeStringTokenizer(lineFromBytes, delimiter, returnDelimiters);

    // Loop through tokens
    while (quoteSafeStringTokenizer.hasMoreElements()
        && quoteSafeStringTokenizer.getPrevTokenIndex() < indexEnd) {

      // Read next token
      String currentToken = quoteSafeStringTokenizer.nextToken();

      // Read info from token
      switch (quoteSafeStringTokenizer.getPrevTokenIndex()) {
        case indexTagId:
          // Store tag id
          tagId = Integer.parseInt(currentToken);

          // Store tag id if lowest or highest seen
          if (lowestTagIdSeen == TagConstants.UNINIT_INT_VAL) {
            lowestTagIdSeen = tagId;
          } else if (tagId < lowestTagIdSeen) {
            lowestTagIdSeen = tagId;
          }

          if (highestTagIdSeen == TagConstants.UNINIT_INT_VAL) {
            highestTagIdSeen = tagId;
          } else if (tagId > highestTagIdSeen) {
            highestTagIdSeen = tagId;
          }
          break;
        case indexName:
          // Remove double quotes from returned tag name and store
          tagName = currentToken.substring(1, currentToken.length() - 1);
          break;
        case indexHistoricalLogging:
          tagHistoricalLoggingEnabled = convertStrToBool(currentToken);
          break;
        case indexGroupA:
          tagInGroupA = convertStrToBool(currentToken);
          break;
        case indexGroupB:
          tagInGroupB = convertStrToBool(currentToken);
          break;
        case indexGroupC:
          tagInGroupC = convertStrToBool(currentToken);
          break;
        case indexGroupD:
          tagInGroupD = convertStrToBool(currentToken);
          break;
        case indexType:
          tagType = Integer.parseInt(currentToken);

          // Type is the last index, form TagInfo object
          TagInfo currentTagInfo = new TagInfo(tagId, tagName, tagHistoricalLoggingEnabled,
              tagInGroupA, tagInGroupB, tagInGroupC, tagInGroupD, tagType);
          tagInfoList[tagInfoListInsertIndex] = currentTagInfo;
          tagInfoListInsertIndex++;
          break;

      }
    }

  }

  /**
   * Convert a string boolean ("0"/"1") to boolean.
   *
   * @param stringBool string boolean ("0"/"1")
   * @return converted boolean
   */
  private static boolean convertStrToBool(String stringBool) {
    return stringBool.equals("1");
  }

  /**
   * Gets the tag info list populated by calling {@link #refreshTagList()}. If this method is called
   * before {@link #refreshTagList()}, an {@link IllegalStateException} will be thrown.
   *
   * @return populated tag information list
   */
  public synchronized static List getTagInfoList() {
    // Verify tag info list has been populated
    if (tagInfoList == null) {
      throw new IllegalStateException(
          "Cannot get tag information list before calling refreshTagList()");
    }

    return Arrays.asList(tagInfoList);
  }

  /**
   * Gets a filtered tag info list containing only tags from the tag info list that are in the
   * specified tag group(s). If this method is called before {@link #refreshTagList()}, an {@link
   * IllegalStateException} will be thrown.
   *
   * @param tagGroups tag groups to include
   * @return filtered tag information list
   */
  public synchronized static List getTagInfoListFiltered(List tagGroups) {
    // Verify tag info list has been populated
    if (tagInfoList == null) {
      throw new IllegalStateException(
          "Cannot get tag information list before calling refreshTagList()");
    }

    /*
     * Create array list to store filtered tags.
     *
     * Use an initial capacity matching the size of full tag list.
     * This will potentially reduce the number of resizing operations
     * of the ArrayList.
     */
    ArrayList filteredTagInfoList = new ArrayList(tagInfoList.length);

    // Loop through each tag in tag info list
    for (int i = 0; i < tagInfoListInsertIndex; i++) {
      // Get tag at array index and its group
      TagInfo currentTagInfo = tagInfoList[i];
      List currentTagGroups = currentTagInfo.getTagGroups();

      // Check if tag belongs to a filter
      boolean filterMatch = false;
      for (int i = 0; i < tagGroups.size(); i++) {
        if (currentTagGroups.contains(tagGroups.get(i))) {
          filterMatch = true;
        }
      }

      // If tag is in desired group(s), add to filtered tag info list
      if (filterMatch) {
        filteredTagInfoList.add(currentTagInfo);
      }
    }

    // Shrink filtered tag array list to remove unused spaces and return
    filteredTagInfoList.trimToSize();
    return filteredTagInfoList;
  }

  /**
   * Gets a filtered tag info list containing only tags from the tag info list that are in the
   * specified tag group(s). If this method is called before {@link #refreshTagList()}, an {@link
   * IllegalStateException} will be thrown.
   *
   * @param tagGroups tag groups to include
   * @return filtered tag information list
   */
  public synchronized static List getTagInfoListFiltered(TagGroup[] tagGroups) {
    return getTagInfoListFiltered(Arrays.asList(tagGroups));
  }

  /**
   * Gets a filtered tag info list containing only tags from the tag info list that are in the
   * specified tag group. If this method is called before {@link #refreshTagList()}, an {@link
   * IllegalStateException} will be thrown.
   *
   * @param tagGroup tag group to include
   * @return filtered tag information list
   */
  public synchronized static List getTagInfoListFiltered(TagGroup tagGroup) {
    return getTagInfoListFiltered(new TagGroup[]{tagGroup});
  }

  /**
   * Gets the lowest tag ID seen during the previous call to {@link #refreshTagList()}. If this
   * method is called before {@link #refreshTagList()}, an {@link IllegalStateException} will be
   * thrown.
   *
   * @return lowest tag ID seen
   */
  public int getLowestTagIdSeen() {
    // Verify lowest tag ID seen variable is set
    if (lowestTagIdSeen == TagConstants.UNINIT_INT_VAL) {
      throw new IllegalStateException(
          "Cannot get lowest tag ID seen before calling refreshTagList()");
    }

    return lowestTagIdSeen;
  }

  /**
   * Gets the highest tag ID seen during the previous call to {@link #refreshTagList()}. If this
   * method is called before {@link #refreshTagList()}, an {@link IllegalStateException} will be
   * thrown.
   *
   * @return highest tag ID seen
   */
  public int getHighestTagIdSeen() {
    // Verify lowest tag ID seen variable is set
    if (lowestTagIdSeen == TagConstants.UNINIT_INT_VAL) {
      throw new IllegalStateException(
          "Cannot get lowest tag ID seen before calling refreshTagList()");
    }

    return highestTagIdSeen;
  }
}
