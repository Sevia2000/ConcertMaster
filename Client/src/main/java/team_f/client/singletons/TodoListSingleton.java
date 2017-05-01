package team_f.client.singletons;

import team_f.client.controls.Home.TodolistHome;

/**
 * Created by w7pro on 29.04.2017.
 */
public class TodoListSingleton{

    private static TodolistHome _todolisthome;

    private TodoListSingleton() {
    }

    public static TodolistHome getInstance() {
        if(_todolisthome == null) {
            _todolisthome = new TodolistHome();
        }

        return _todolisthome;
    }
}

