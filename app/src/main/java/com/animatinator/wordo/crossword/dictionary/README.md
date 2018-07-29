# Crossword word set generator

This module generates a list of words usable in a crossword-style puzzle in
which all words must be formable from the same finite set of letters (probably
around six or seven).

It takes an arbitrary dictionary file as input and can generate puzzles with
configurable minimum word length, maximum word length and maximum number of
words.

The `data` folder contains some sample dictionaries for experimentation.

## Usage
```
>> Main /data/top5000.txt

PuzzleConfiguration{letter set={ c, e, e, l, r, t, u, }; words=[true, elect, etc, rule, cute, cut, let, rule, lecture, clue]}
```