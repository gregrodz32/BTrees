# B-Tree Project

## Overview

This project implements a command-line B-tree index file manager in Java. It creates, inserts, searches, and extracts key/value pairs using a B-tree structure stored on disk in 512-byte blocks. Each index file starts with a 512-byte header, followed by node blocks.

## Features
The B-tree has a **minimum degree of 10**, which allows:
- Up to **19 key/value pairs** per node
- **20 child pointers**


## Workflow

# Create a new index
java Main create demo.idx

# Insert some entries
java Main insert demo.idx 10 100
java Main insert demo.idx 20 200
java Main insert demo.idx 15 150

# Search for a key
java Main search demo.idx 15
# → Found key: 15 value: 150

# Print contents
java Main print demo.idx
# → 10 -> 100
#   15 -> 150
#   20 -> 200

# Bulk load from CSV
java Main load demo.idx input.csv
java Main print demo.idx

# Extract to CSV
java Main extract demo.idx output.csv
cat output.csv
