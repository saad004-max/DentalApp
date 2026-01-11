package ma.emsi.repository.api.cabinet;

import java.util.List;
import ma.emsi.entities.modules.cabinet.Notification;
import ma.emsi.entities.modules.enums.PrioriteNotification;
import ma.emsi.entities.modules.enums.TypeNotification;
import ma.emsi.repository.api.common.CrudRepo;

/**
 * Repository pour la gestion des notifications.
 */
public interface NotificationRepo extends CrudRepo<Notification, Long> {
    
    /**
     * Trouve les notifications par type.
     */
    List<Notification> findByType(TypeNotification type);
    
    /**
     * Trouve les notifications par priorité.
     */
    List<Notification> findByPriorite(PrioriteNotification priorite);
    
    /**
     * Trouve les dernières notifications (limitées).
     */
    List<Notification> findRecent(int limit);
    
    /**
     * Marque une notification comme lue.
     */
    void markAsRead(Long notificationId);
}
