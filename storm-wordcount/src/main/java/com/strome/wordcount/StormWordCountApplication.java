package com.strome.wordcount;

import com.strome.wordcount.bolt.SplitSentenceBolt;
import com.strome.wordcount.bolt.WordCountBolt;
import com.strome.wordcount.spout.RandomSentenceSpout;
import com.strome.wordcount.utils.StormUtil;
import org.apache.storm.Config;
import org.apache.storm.LocalCluster;
import org.apache.storm.StormSubmitter;
import org.apache.storm.topology.TopologyBuilder;
import org.apache.storm.tuple.Fields;

public class StormWordCountApplication {
    public static void main(String[] args) throws Exception {
        TopologyBuilder topologyBuilder = new TopologyBuilder();
        topologyBuilder.setSpout("spout", new RandomSentenceSpout(), 1);
        topologyBuilder.setBolt("splitbolt", new SplitSentenceBolt(), 1).shuffleGrouping("spoput");
        topologyBuilder.setBolt("count", new WordCountBolt(), 1).fieldsGrouping("splitbolt", new Fields("word"));

        Config config = new Config();
        config.setDebug(true);

        if (args != null && args.length > 0) {
            config.setNumWorkers(3);
            StormSubmitter.submitTopologyWithProgressBar(args[0], config, topologyBuilder.createTopology());
        } else {
            config.setMaxTaskParallelism(3);
            LocalCluster localCluster = new LocalCluster();
            localCluster.submitTopology("word-count", config, topologyBuilder.createTopology());
            StormUtil.sleep(1000);
            localCluster.shutdown();
        }
    }
}
