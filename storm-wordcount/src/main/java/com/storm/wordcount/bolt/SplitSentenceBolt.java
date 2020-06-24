package com.storm.wordcount.bolt;

import org.apache.storm.task.OutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseRichBolt;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Tuple;
import org.apache.storm.tuple.Values;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * @Desc: 字符串 切割成 字符 Bolt(代码组件)
 * @Date: 2020-06-06
 * @Version:v1.0
 */
public class SplitSentenceBolt extends BaseRichBolt {

    private Logger logger = LoggerFactory.getLogger(SplitSentenceBolt.class);

    private OutputCollector outputCollector;

    @Override
    public void prepare(Map<String, Object> topoConf, TopologyContext context, OutputCollector collector) {
        this.outputCollector = collector;
    }

    @Override
    public void execute(Tuple input) {
        String sentence = input.getStringByField("sentence");
        // 将句子已空格切割成字符
        String[] strArray = sentence.split(" ");

        logger.info("SplitSentenceBolt.execute process, 根据空格切割字符串 ,sentence : {}", sentence);

        for (String s : strArray) {
            this.outputCollector.emit(new Values(s));
        }

    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer declarer) {
        declarer.declare(new Fields("word"));
    }
}
