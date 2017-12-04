package adventofcode2017.potasz

// http://adventofcode.com/2017/day/2
object Puzzle02 {
    val input = "116\t1470\t2610\t179\t2161\t2690\t831\t1824\t2361\t1050\t2201\t118\t145\t2275\t2625\t2333\n" +
            "976\t220\t1129\t553\t422\t950\t332\t204\t1247\t1092\t1091\t159\t174\t182\t984\t713\n" +
            "84\t78\t773\t62\t808\t83\t1125\t1110\t1184\t145\t1277\t982\t338\t1182\t75\t679\n" +
            "3413\t3809\t3525\t2176\t141\t1045\t2342\t2183\t157\t3960\t3084\t2643\t119\t108\t3366\t2131\n" +
            "1312\t205\t343\t616\t300\t1098\t870\t1008\t1140\t1178\t90\t146\t980\t202\t190\t774\n" +
            "4368\t3905\t3175\t4532\t3806\t1579\t4080\t259\t2542\t221\t4395\t4464\t208\t3734\t234\t4225\n" +
            "741\t993\t1184\t285\t1062\t372\t111\t118\t63\t843\t325\t132\t854\t105\t956\t961\n" +
            "85\t79\t84\t2483\t858\t2209\t2268\t90\t2233\t1230\t2533\t322\t338\t68\t2085\t1267\n" +
            "2688\t2022\t112\t130\t1185\t103\t1847\t3059\t911\t107\t2066\t1788\t2687\t2633\t415\t1353\n" +
            "76\t169\t141\t58\t161\t66\t65\t225\t60\t152\t62\t64\t156\t199\t80\t56\n" +
            "220\t884\t1890\t597\t3312\t593\t4259\t222\t113\t2244\t3798\t4757\t216\t1127\t4400\t178\n" +
            "653\t369\t216\t132\t276\t102\t265\t889\t987\t236\t239\t807\t1076\t932\t84\t864\n" +
            "799\t739\t75\t1537\t82\t228\t69\t1397\t1396\t1203\t1587\t63\t313\t1718\t1375\t469\n" +
            "1176\t112\t1407\t136\t1482\t1534\t1384\t1202\t604\t851\t190\t284\t1226\t113\t114\t687\n" +
            "73\t1620\t81\t1137\t812\t75\t1326\t1355\t1545\t1666\t1356\t1681\t1732\t85\t128\t902\n" +
            "571\t547\t160\t237\t256\t30\t496\t592\t385\t576\t183\t692\t192\t387\t647\t233\n"

    val sample1 = "5\t1\t9\t5\n" +
            "7\t5\t3\n" +
            "2\t4\t6\t8\n"

    val sample2 = "5\t9\t2\t8\n9\t4\t7\t3\n3\t8\t6\t5\n"

    private fun String.convert(): Array<Array<Int>> {
        return this
                .split("\n")
                .dropLastWhile { it.isEmpty() }
                .map { it.split("\t").map { it.toInt() } }
                .map { it.sortedBy { -it }.toTypedArray() }
                .toTypedArray()
    }

    fun solve1(input: Array<Array<Int>>): Int = input.map { it.first() - it.last()}.sum()

    fun solve2(input: Array<Array<Int>>): Int {
        return input.map { row ->
            row.mapIndexed { i, n ->
                (i+1 until row.size).map { j -> n / row[j].toDouble() }.filter { it % 1 == 0.0 }
            }.flatMap { it }
        }.flatMap { it }.sum().toInt()
    }

    @JvmStatic
    fun main(args: Array<String>) {
        println(solve1(sample1.convert()))
        println(solve1(input.convert()))

        println(solve2(sample2.convert()))
        println(solve2(input.convert()))
    }
}
