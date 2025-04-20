package com.shoppingmall.demo.service.common;

import com.huaban.analysis.jieba.JiebaSegmenter;
import com.huaban.analysis.jieba.SegToken;
import com.shoppingmall.demo.constant.CacheConstants;
import com.shoppingmall.demo.constant.MessageConstants;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class SearchProcessService {
    public final RedisCacheService redisCacheService;


    public List<Long> getIdList(List<String> wordList, String preKey) {
        List<Set<String>> setList = new ArrayList<>();
        for (String word : wordList) {
            Set<String> songIdSet = redisCacheService.getElementsInRange(preKey + word, 0, -1);
            setList.add(songIdSet);
        }
        if(setList.isEmpty()){ return Collections.emptyList(); }
        Set<String> result = setList.get(0);
        for (int i = 1; i < setList.size(); i++) {
            result.retainAll(setList.get(i));
        }
        List<Long> ids = new ArrayList<>();
        for (String songId : result) {
            ids.add(Long.valueOf(songId));
        }
        if (ids.isEmpty()) {
            return Collections.emptyList();
        }
        return ids;
    }

    public  List<String> getWordList(String text) throws IOException {
        char[] charArray = text.toCharArray();
        List<String> charList = new ArrayList<>();
        for (char chr : charArray) {
            charList.add(String.valueOf(chr));
        }

        JiebaSegmenter segment = new JiebaSegmenter();
        List<SegToken> process = segment.process(text, JiebaSegmenter.SegMode.SEARCH);
        List<String> prewordList = new ArrayList<>(process.stream().map(e -> e.word).toList());
        prewordList.addAll(charList);
        List<String> wordList = new ArrayList<>(prewordList.stream().distinct().toList());

        List<String> stopWords = getStopWords();

        //File stopWordTxt = ResourceUtils.getFile(MessageConstants.FILEPATH_STOP_WORDS_TXT);
        //List<String> stopWords = FileUtils.readLines(stopWordTxt, "utf8");

        wordList.removeAll(stopWords);
        wordList.remove(" ");
        return wordList;
    }

    private static List<String> getStopWords() throws IOException {
        ClassPathResource classpathResource = new ClassPathResource(CacheConstants.CLASSPATH_STOP_WORDS_TXT);
        List<String> stopWords;
        try (
                //使用流来处理
                InputStream is = classpathResource.getInputStream();
                BufferedReader bf = new BufferedReader(new InputStreamReader(is));
        ) {
            String str;
            stopWords = new ArrayList<>();
            while ((str = bf.readLine()) != null) {
                //按行处理
                stopWords.add(str);
            }
        }
        return stopWords;
    }
}
