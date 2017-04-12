package me.diax.srv.stubs.service;

import me.diax.srv.stubs.model.IdentifiableModel;

interface CrudService<T extends IdentifiableModel> {

    T getById(long id);

    void save();

}
