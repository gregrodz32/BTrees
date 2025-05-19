Project 3 GXR2300010 Devlog

## 05/01/2025

Read through the project file and created the skeleton of my project. I made a main.java that will serve as my command-line dispatcher, indexfile.java that will be used for file creation, I/O, and header handling
BTree will be the logic for search and insert
Utils.java will be a helper to convert long bytes

Tomorrow I want to work on the Index file and fill in the commands that will be needed

## 05/03/2025

Implemented the 'create' commanding in IndexFile.java

- It writes 512-byte header
header values written using 'Utils.longToBytes()'
- I confirmed the file size and ouput using hexdump

The next day will be completing the deign node structure 

## 05/06/2025

Today I implemented the BTreeNode

- Created BTreeNode with 19 keys, values, child pointers, block ID, parent ID, and key count.
- Added serialization to convert the node to a 512-byte array.
- Updated create to add an empty root node at offset 512.
- Verified with hexdump.

The next day I will work on inserting a key value into the root to update the node

## 05/08/2025

Insert + Search in root node (no split implemented yet)

- Implemented `insert()` to load root, add key/value pair, and write node back.
- Enforced sort order by inserting at correct index.
- `search()` loads root node, scans for key, and prints result or "not found".

The next day I want to implement full insert with node splits and print command.

## 05/11/2025

- Implemented `print` function to display all key/value pairs from the root node in sorted order.
- Added `load` function to read from a CSV file and insert key/value pairs into the B-tree.
- Added `extract` function to export all key/value pairs to a CSV file.
- Tested all major commands:
  - `create`, `insert`, `search`, `print`, `load`, `extract`
- Created an example `input.csv` file for `load` testing and verified the extracted `output.csv`.
- Wrote README.md to explain compilation, usage, file structure, and known limitations.
- Project is ready for submission. Made final git commit

Everything is ready for turn in