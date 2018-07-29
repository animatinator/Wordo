import json
from PIL import Image, ImageDraw, ImageFont
import sys

grid_square_size = 50
font = ImageFont.truetype("arial.ttf", 40)


def main(board_to_print):
    grid_width = len(board_to_print[0])
    grid_height = len(board_to_print)
    image_width = grid_width * grid_square_size
    image_height = grid_height * grid_square_size

    image = Image.new(mode='L', size=(image_width, image_height), color=255)

    draw = ImageDraw.Draw(image)
    draw_grid(draw, grid_width, grid_height, image_width, image_height)
    draw_letters(draw, board_to_print)

    image.show("test")


def draw_grid(draw, vertical, horizontal, width, height):
    # Vertical lines
    for x in range(0, vertical):
        xPos = x * grid_square_size
        draw.line(((xPos, 0), (xPos, height)), fill=128)

    # Horizontal lines
    for y in range(0, horizontal):
        yPos = y * grid_square_size
        draw.line(((0, yPos), (width, yPos)), fill=128)


def draw_letters(draw, board):
    for y in range(0, len(board)):
        for x in range(0, len(board[0])):
            value = board[y][x]
            if value:
                # Draw the letter centered in position
                text_width, text_height = draw.textsize(value, font)
                x_offset = (grid_square_size - text_width) / 2
                y_offset = (grid_square_size - text_height) / 2
                draw.text((x * grid_square_size + x_offset, y * grid_square_size + y_offset), value, font=font)


if __name__ == '__main__':
    args = sys.argv
    if len(args) < 2:
        print("ERROR: Need to supply a board to be printed!")
    elif len(args) > 2:
        print("ERROR: Too many args! This script only takes one argument, the board to be printed.")
    else:
        parsed = json.loads(args[1])
        main(parsed)
