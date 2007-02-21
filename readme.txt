Version 0.3

--0.1 Production ready support for basic alphanumerics, basic signed and unsigned decimal with support for signed separate.

--0.2 Added support for binary and packed decimal.

--0.3
    Added support for floating types (comp-1, comp-2) only IEEE-754 supported
    Added support little-endian binary (comp-5).  See copybook.setLittleEndian.
    Cleaned up method names.  Made it consistent by adding 'get' to 'getter' methods even though I kind of hate it.
    Made Data a real base class.
    