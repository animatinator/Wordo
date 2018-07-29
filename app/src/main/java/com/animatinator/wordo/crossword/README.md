# Crossword generator

Generates a crossword given a list of words. Can output to JSON format for
convenience - see sample output below.

There are various ways of configuring this - see `BoardGenerationFlagConstant`
for the list of flags which can be used.

There's also a Python script, `scripts/draw_board.py`, which will generate
an image of a given JSON-represented board.

Sample output:
```
Board:
.......u....
.......s....
......sea.u.
.....c..case
.c..cause.e.
dues.u..s.d.
.e...sad....
.sauce......
..d..d......
..sue.......

JSON:
[[null, null, null, null, null, null, null, \"u\", null, null, null, null], [null, null, null, null, null, null, null, \"s\", null, null, null, null], [null, null, null, null, null, null, \"s\", \"e\", \"a\", null, \"u\", null], [null, null, null, null, null, \"c\", null, null, \"c\", \"a\", \"s\", \"e\"], [null, \"c\", null, null, \"c\", \"a\", \"u\", \"s\", \"e\", null, \"e\", null], [\"d\", \"u\", \"e\", \"s\", null, \"u\", null, null, \"s\", null, \"d\", null], [null, \"e\", null, null, null, \"s\", \"a\", \"d\", null, null, null, null], [null, \"s\", \"a\", \"u\", \"c\", \"e\", null, null, null, null, null, null], [null, null, \"d\", null, null, \"d\", null, null, null, null, null, null], [null, null, \"s\", \"u\", \"e\", null, null, null, null, null, null, null]]
```