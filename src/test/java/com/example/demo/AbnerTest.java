package com.example.demo;

import abner.Tagger;
import com.example.demo.testdata.Text;

public class AbnerTest {

    private static Tagger tagger = new Tagger();

    public static void main(String[] args) {
//        String[] proteins = tagger.getEntities(Text.example, "PROTEIN");
//        for(String protein : proteins)
//        System.out.println(protein);
        String[][] entities = tagger.getEntities(Text.example);//二维数组，第一个为实体数组，第二个为类别数组
        System.out.println(entities.length);
        String[] es = entities[0];
        String[] types = entities[1];
        System.out.println(es.length);
        for(int i = 0;i<es.length; i++){
            //System.out.println(es[i]+"\t"+types[i]);
        }

        String s = tagger.tagABNER(Text.example2);
        //System.out.println(s);

    }

}
