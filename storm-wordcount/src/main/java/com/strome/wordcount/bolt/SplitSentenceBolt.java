package com.strome.wordcount.bolt;

import org.apache.storm.task.OutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseRichBolt;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Tuple;
import org.apache.storm.tuple.Values;
import org.springframework.util.StringUtils;

import java.util.Map;

/**
 * @Desc: 字符串 切割成 字符 Bolt(代码组件)
 * @Date: 2020-06-06
 * @Version:v1.0
 */
public class SplitSentenceBolt extends BaseRichBolt {

    private OutputCollector outputCollector;

    @Override
    public void prepare(Map<String, Object> topoConf, TopologyContext context, OutputCollector collector) {
        this.outputCollector = collector;
    }

    @Override
    public void execute(Tuple input) {
        String sentence = input.getString(0);
        if (StringUtils.isEmpty(sentence)) {
            return;
        }

        // 将句子已空格切割成字符
        String[] strArray = sentence.split(" ");
        for (String s : strArray) {
            this.outputCollector.emit(new Values(s));
        }

    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer declarer) {
        declarer.declare(new Fields("word"));
    }
}
