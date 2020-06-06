package com.strome.wordcount.spout;

import com.strome.wordcount.utils.StormUtil;
import org.apache.storm.spout.SpoutOutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseRichSpout;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Values;

import java.util.Map;
import java.util.Random;

/**
 * @Desc: 字数统计 数据来源
 * @Date: 2020-06-06
 * @Version:v1.0
 */
public class RandomSentenceSpout extends BaseRichSpout {

    private SpoutOutputCollector spoutOutputCollector;

    private Random random;

    @Override
    public void open(Map<String, Object> map, TopologyContext topologyContext, SpoutOutputCollector spoutOutputCollector) {
        this.spoutOutputCollector = spoutOutputCollector;
        this.random = new Random();
    }

    @Override
    public void nextTuple() {

        StormUtil.sleep(100);

        String[] sentences = new String[]{sentence("the cow jumped over the room"), sentence("an apple a day keeps the doctor away"),
                sentence("four score and seven years ago"), sentence("snow white and the seven dwarfs"),
                sentence("i am at two with nature")};

        /** 随机获取字符串数组中的串句子*/
        final String sentence = sentences[random.nextInt(sentences.length - 1)];

        /** 将字符串发送出去*/
        spoutOutputCollector.emit(new Values(sentence));
    }

    /**
     * @Desc: 声明 spout 发送出去的 名称
     * @Param outputFieldsDeclarer
     * @Return void
     * @Date: 2020/6/6
     */
    @Override
    public void declareOutputFields(OutputFieldsDeclarer outputFieldsDeclarer) {
        outputFieldsDeclarer.declare(new Fields("sentence"));
    }

    protected String sentence(String input) {
        return input;
    }

    @Override
    public Map<String, Object> getComponentConfiguration() {
        return super.getComponentConfiguration();
    }
}
