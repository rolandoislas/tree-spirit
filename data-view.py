import json
import os
import sys

import time


def print_info(prefix, block):
    print(prefix, "dimension:", block["dimension"], "pos", block["pos"], "player",
                                                                block["playerUid"], " ")


def display_file():
    data_file = open(sys.argv[1], "r")
    lines = data_file.readlines()
    data_json_str = ""
    for line in lines:
        data_json_str += line
    worlds = json.loads(data_json_str)["worlds"]
    print("Worlds:", len(worlds), "\n", " ")
    for worldId in worlds:
        world = worlds[worldId]
        print("World:", worldId, " ")
        saplings = world["saplings"]
        cores = world["cores"]
        dimension_cores = world["dimensionCores"]
        death_timers = world["deathTimers"]
        players = world["players"]
        print("Saplings:", len(saplings), " ")
        print("Cores:", len(cores), " ")
        print("Dimension Cores", len(dimension_cores), " ")
        print("Death Timers:", len(death_timers), " ")
        print("Players:", len(players), "\n", " ")
        for sapling in saplings:
            print_info("Sapling:", sapling)
        for core in cores:
            print_info("Core:", core)
        for dimension in dimension_cores:
            print_info("Dimension Core:", dimension)
        print("\n\n")


def clear():
    os.system("cls" if os.name == "nt" else "clear")


while True:
    clear()
    try:
        display_file()
    except Exception as e:
        print(e)
    time.sleep(1)
