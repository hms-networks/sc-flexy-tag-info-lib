# Ewon Flexy Tag Info Library Changelog

## v1.2.1
### Major Changes
### Minor Changes
- Add method to get TagInfo object by tag ID

## v1.2
### Major Changes
- Add integer mapped string tag type
- Add tag description to tag info objects
### Minor Changes
- Add var_lst line number and tag name to max capacity error message
- Corrected improper developer javadoc documentation

## v1.1
### Major Changes
- Feature: Replaced static buffer for tag info with resizable ByteArrayOutputStream
- API Change: TagInfoManger.refreshTagList() now throws TagInfoBufferException if tag description line length exceeds MAX_CAPACITY_BYTES
   * TagInfoBufferException extends IOException, existing library users that catch IOException, will also catch this exception
   * Users that want to differentiate TagInfoBufferException from IOException, should order TagInfoBufferException catch first

## v1.0
Initial release
