package com.storm.wordcount.topology;

import com.storm.wordcount.bolt.SplitSentenceBolt;
import com.storm.wordcount.bolt.WordCountBolt;
import com.storm.wordcount.spout.RandomSentenceSpout;
import com.storm.wordcount.utils.StormUtil;
import org.apache.storm.Config;
import org.apache.storm.LocalCluster;
import org.apache.storm.StormSubmitter;
import org.apache.storm.topology.TopologyBuilder;
import org.apache.storm.tuple.Fields;

/**
 * @Desc: 字串统计 拓扑
 * @Date: 2020-06-06
 * @Version:v1.0
 */
public class WordCountTopology {

    public static void main(String[] args) throws Exception {
        TopologyBuilder topologyBuilder = new TopologyBuilder();
        topologyBuilder.setSpout("SENTENCE", new RandomSentenceSpout(), 2);
        topologyBuilder.setBolt("SPLIT", new SplitSentenceBolt(), 5)
                .setNumTasks(10)
                .shuffleGrouping("SENTENCE");
        topologyBuilder.setBolt("WORDCOUNT", new WordCountBolt(), 10)
                .setNumTasks(20)
                .fieldsGrouping("SPLIT", new Fields("word"));
        Config config = new Config();
        if (args != null && args.length > 0) {
            config.setNumWorkers(3);
            try {
                StormSubmitter.submitTopologyWithProgressBar(args[0], config, topologyBuilder.createTopology());
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            config.setMaxTaskParallelism(3);
            LocalCluster localCluster = new LocalCluster();
            localCluster.submitTopology("WordCountTopology", config, topologyBuilder.createTopology());
            StormUtil.sleep(60000);
            localCluster.shutdown();
        }
    }
}
