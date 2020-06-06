package com.strome.wordcount.bolt;

import org.apache.storm.task.OutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseRichBolt;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Tuple;
import org.apache.storm.tuple.Values;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Desc: 字符统计 Bolt（代码组件）
 * @Date: 2020-06-06
 * @Version:v1.0
 */
public class WordCountBolt extends BaseRichBolt {

    /**
     * 发送组件
     */
    private OutputCollector outputCollector;

    /**
     * 字数统计Map
     */
    Map<String, Integer> wordCountMap = new ConcurrentHashMap<String, Integer>();


    @Override
    public void prepare(Map<String, Object> topoConf, TopologyContext context, OutputCollector collector) {
        this.outputCollector = collector;
    }

    @Override
    public void execute(Tuple input) {
        String word = input.getString(0);
        Integer count = wordCountMap.get(word);
        if (count == null) {
            count = 1;
        } else {
            count++;
        }
        wordCountMap.put(word, count);
        // 将统计结果发送出去
        outputCollector.emit(new Values(word, count));
    }


    @Override
    public void declareOutputFields(OutputFieldsDeclarer declarer) {
        declarer.declare(new Fields("word", "count"));
    }
}
