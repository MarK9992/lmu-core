package org.lucci.lmu.model;

import java.util.Collection;
import java.util.Set;

/**
 * Created by Benjamin on 04/01/2016.
 */
public interface IModel {
	Collection<Collection<Entity>> getAlignments();
	Set<Entity> getEntities();
	Set<Relation> getRelations();
	Collection<Group> getGroups();

	Collection<? extends Relation> removeEntity(Entity entity);
	void addRelation(Relation rel);
	void addEntity(Entity entity);

	void merge(IModel newModel);


}
