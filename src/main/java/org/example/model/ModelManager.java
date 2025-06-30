package org.example.model;

import java.util.*;

public class ModelManager {
    private static final Map<TitoloBacheca, Bacheca> bacheche = new EnumMap<>(TitoloBacheca.class);

    public static boolean creaBacheca(TitoloBacheca titolo, String descrizione) {
        if (bacheche.containsKey(titolo)) return false;
        bacheche.put(titolo, new Bacheca(titolo, descrizione));
        return true;
    }

    public static Map<TitoloBacheca, Bacheca> getBacheche() {
        return bacheche;
    }

    public static Bacheca getBacheca(TitoloBacheca tipo) {
        return bacheche.get(tipo);
    }

    public static boolean aggiornaDescrizione(TitoloBacheca titolo, String descrizione) {
        Bacheca b = bacheche.get(titolo);
        if (b != null) {
            b.setDescrizione(descrizione);
            return true;
        }
        return false;
    }

    public static boolean eliminaBacheca(TitoloBacheca titolo) {
        return bacheche.remove(titolo) != null;
    }

    public static List<ToDo> getToDoPerBacheca(TitoloBacheca titolo) {
        Bacheca bacheca = bacheche.get(titolo);
        return bacheca != null ? bacheca.getToDoList() : new ArrayList<>();
    }

    public static void aggiungiToDo(TitoloBacheca titolo, ToDo todo) {
        Bacheca b = bacheche.get(titolo);
        if (b != null) {
            b.aggiungiToDo(todo);
        }
    }

    public static void rimuoviToDo(TitoloBacheca titolo, ToDo todo) {
        Bacheca b = bacheche.get(titolo);
        if (b != null) {
            b.getToDoList().remove(todo);
        }
    }
    public static void spostaToDo(TitoloBacheca bacheca, ToDo todo, int nuovaPosizione) {
        Bacheca b = bacheche.get(bacheca);
        if (b != null && b.getToDoList().contains(todo)) {
            List<ToDo> lista = b.getToDoList();
            lista.remove(todo);
            if (nuovaPosizione < 0) nuovaPosizione = 0;
            if (nuovaPosizione > lista.size()) nuovaPosizione = lista.size();
            lista.add(nuovaPosizione, todo);
            aggiornaPosizioni(lista);
        }
    }

    private static void aggiornaPosizioni(List<ToDo> lista) {
        for (int i = 0; i < lista.size(); i++) {
            lista.get(i).setPosizione(i);
        }
    }

}
