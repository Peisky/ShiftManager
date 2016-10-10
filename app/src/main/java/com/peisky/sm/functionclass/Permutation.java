package com.peisky.sm.functionclass;

import java.util.ArrayList;
import java.util.List;

public class Permutation {
	public static <T> List<T> rotatedTo(int i, List<T> list) {
        List<T> rotated = new ArrayList<T>();
        rotated.add(list.get(i));
        rotated.addAll(list.subList(0, i));
        rotated.addAll(list.subList(i + 1, list.size()));
        return rotated;
    }

    public static <T> List<List<T>> allRotated(List<T> list) {
        List<List<T>> allRotated = new ArrayList<List<T>>();
        for(int i = 0; i < list.size(); i++) {
            allRotated.add(rotatedTo(i, list));
        }
        return allRotated;
    }
            
    public static <T> List<List<T>> perm(List<T> list) {
        List<List<T>> pls = new ArrayList<List<T>>();
        
        if(list.isEmpty()) {
            pls.add(new ArrayList<T>());
        } else {
            for(List<T> lt : allRotated(list)) {
                for(List<T> tailPl : perm(lt.subList(1, lt.size()))) {
                    List<T> pl = new ArrayList<T>();
                    pl.add(lt.get(0));
                    pl.addAll(tailPl);
                    pls.add(pl);
                }
            }
        }
        
        return pls;
    }
   
}
