package fr.eseo.tauri.seeder;

import fr.eseo.tauri.model.Notification;
import fr.eseo.tauri.model.User;
import fr.eseo.tauri.model.enumeration.NotificationType;
import fr.eseo.tauri.repository.NotificationRepository;
import fr.eseo.tauri.repository.UserRepository;
import net.datafaker.Faker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class NotificationSeeder {

    private final NotificationRepository notificationRepository;

    private final UserRepository userRepository;

    @Autowired
    public NotificationSeeder(NotificationRepository notificationRepository, UserRepository userRepository) {
        this.notificationRepository = notificationRepository;
        this.userRepository = userRepository;
    }

    public void seed(Faker faker) {
        List<User> users = userRepository.findAll();
        List<Integer> usersId = new ArrayList<Integer>();
        for ( int i=0; i<users.size(); i++){
            usersId.add(users.get(i).id());
        }

        for ( int i=0; i<20; i++){
            Notification notification = new Notification();

            int userId1 = faker.number().numberBetween(0, users.size()-1);
            int userId2 = faker.number().numberBetween(0, users.size()-1);

            String MESSAGE = "La composition des équipes a été prépubliée.";
            notification.message(MESSAGE);
            notification.type(NotificationType.valueOf("CREATE_TEAMS"));
            notification.isRead(faker.bool().bool());
            notification.userTo(users.get(usersId.get(userId1)));
            notification.userFrom(users.get(usersId.get(userId2)));

            notificationRepository.save(notification);
        }
    }
}
