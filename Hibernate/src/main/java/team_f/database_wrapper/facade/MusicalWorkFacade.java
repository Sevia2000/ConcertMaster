package team_f.database_wrapper.facade;

import team_f.database_wrapper.entities.EventDutyMusicalWorkEntity;
import team_f.database_wrapper.entities.MusicalWorkEntity;
import team_f.domain.entities.Instrumentation;
import team_f.domain.entities.MusicalWork;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.List;

public class MusicalWorkFacade extends BaseDatabaseFacade<MusicalWorkEntity, MusicalWork> {
    private static InstrumentationFacade _instrumentationFacade = new InstrumentationFacade();

    public MusicalWorkFacade() {
        super();
    }

    public MusicalWorkFacade(EntityManager session) {
        super(session);
    }

    public Integer addMusicalWork(MusicalWork musicalWork) {
        EntityManager session = getCurrentSession();
        session.getTransaction().begin();

        MusicalWorkEntity mwEntity = convertToMusicalWorkEntity(musicalWork);
        Instrumentation instrumentation = musicalWork.getInstrumentation();
        mwEntity.setInstrumentationId(_instrumentationFacade.addInstrumentation(instrumentation));

        if (mwEntity.getMusicalWorkId() > 0) {
            session.merge(mwEntity);
        } else {
            session.persist(mwEntity);
            session.flush();
        }

        try {
            session.flush();
            session.getTransaction().commit();
        } catch (Exception e) {
            session.getTransaction().rollback();
        }

        return mwEntity.getMusicalWorkId();
    }

    public MusicalWork getMusicalWorkById(int id) {
        EntityManager session = getCurrentSession();

        // prevent SQL injections
        Query query = session.createQuery("from MusicalWorkEntity where :id");
        query.setParameter("id", id);
        query.setMaxResults(1);

        List<MusicalWorkEntity> instrumentations = query.getResultList();
        MusicalWorkEntity musicalWorkEntity;
        MusicalWork musicalWork = new MusicalWork();

        if (instrumentations.size() > 0) {
            musicalWorkEntity = instrumentations.get(0);
            musicalWork = convertToMusicalWork(musicalWorkEntity);
        }

        return musicalWork;
    }

    public List<MusicalWork> getMusicalWorks() {
        EntityManager session = getCurrentSession();

        // prevent SQL injections
        Query query = session.createQuery("from MusicalWorkEntity");

        List<MusicalWorkEntity> musicalWorkEntities = query.getResultList();
        List<MusicalWork> musicalWorks = new ArrayList<>();

        for (MusicalWorkEntity entity : musicalWorkEntities) {
            MusicalWork musicalWork = convertToMusicalWork(entity);

            musicalWorks.add(musicalWork);
        }

        return musicalWorks;
    }


    public List<MusicalWork> getMusicalWorksForEvent(int eventId) {
        EntityManager session = getCurrentSession();

        Query query = session.createQuery("from EventDutyMusicalWorkEntity where eventDuty = :id");
        query.setParameter("id", eventId);

        List<MusicalWork> mwList = new ArrayList<>();

        List<EventDutyMusicalWorkEntity> eList = query.getResultList();
        MusicalWork musicalWork;
        InstrumentationFacade instrumentationFacade = new InstrumentationFacade(getCurrentSession());

        for (EventDutyMusicalWorkEntity edmwe : eList) {

            Query musicalQuery = session.createQuery("from MusicalWorkEntity where musicalWorkId = :id");
            musicalQuery.setParameter("id", edmwe.getMusicalWork());


            List<MusicalWorkEntity> mweList = musicalQuery.getResultList();

            for (MusicalWorkEntity mwe : mweList) {
                musicalWork = convertToMusicalWork(mwe);

                // set the alternative instrumentation on this object to avoid unnecessary lookups in the DB
                if(edmwe.getAlternativeInstrumentation() != null) {
                    musicalWork.setAlternativeInstrumentation(instrumentationFacade.getInstrumentationByID(edmwe.getAlternativeInstrumentation()));
                }

                mwList.add(musicalWork);
            }
        }

        return mwList;
    }

    /**
     * Function to convert MusicalWorkEntity Object to MusicalWork. Returns the MusicalWork after creating and setting Informations from MusicalWorkEntity object.
     * @return musicalWork     MusicalWork        returns MusicalWork Object
     */
    protected MusicalWork convertToMusicalWork(MusicalWorkEntity mwe) {
        MusicalWork musicalWork = new MusicalWork();
        musicalWork.setMusicalWorkID(mwe.getMusicalWorkId());

        InstrumentationFacade instrumentationFacade = new InstrumentationFacade(getCurrentSession());
        musicalWork.setInstrumentation(instrumentationFacade.getInstrumentationByID(mwe.getInstrumentationId()));
        musicalWork.setComposer(mwe.getComposer());
        musicalWork.setName(mwe.getName());

        return musicalWork;
    }

    /**
     * Function to convert MusicalWork Object to MusicalWorkEntity. Returns the MusicalWorkEntity after creating and setting Informations from MusicalWork object.
     * @return musicalworkentity     MusicalWorkEntity         returns MusicalWorkEntity Object
     */
    protected MusicalWorkEntity convertToMusicalWorkEntity(MusicalWork mw) {
        MusicalWorkEntity mwe = new MusicalWorkEntity();

        mwe.setMusicalWorkId(mw.getMusicalWorkID());
        mwe.setComposer(mw.getComposer());
        mwe.setName(mw.getName());

        return mwe;
    }


    @Override
    public int add(MusicalWork value) {
        return this.addMusicalWork(value);
    }

    @Override
    public int update(MusicalWork value) {
        EntityManager session = getCurrentSession();
        session.getTransaction().begin();
        int returnId;

        MusicalWorkEntity mwe = convertToMusicalWorkEntity(value);

        if (mwe.getMusicalWorkId() > 0){
            mwe = session.merge(mwe);
            returnId = mwe.getMusicalWorkId();
        }else{
            returnId = this.add(value);
        }
        try {
            session.flush();
            session.getTransaction().commit();
        } catch (Exception e) {
            session.getTransaction().rollback();
        }
        return returnId;
    }

    @Override
    public boolean delete(int id) {
        boolean b;
        EntityManager session = getCurrentSession();
        session.getTransaction().begin();

        MusicalWork mw = getMusicalWorkById(id);
        session.remove(convertToMusicalWorkEntity(mw));

        try {
            session.flush();
            session.getTransaction().commit();
            b = true;
        } catch (Exception e) {
            session.getTransaction().rollback();
            b = false;
        }
        return b;
    }
}
