# Ewon Flexy Tag Info Library Changelog

## v1.1
### Major Changes
- Feature: Replaced static buffer for tag info with resizable ByteArrayOutputStream
- API Change: TagInfoManger.refreshTagList() now throws TagInfoBufferException if tag description line length exceeds MAX_CAPACITY_BYTES
   * TagInfoBufferException extends IOException, existing library users that catch IOException, will also catch this exception
   * Users that want to differentiate TagInfoBufferException from IOException, should order TagInfoBufferException catch first

## v1.0
Initial release
