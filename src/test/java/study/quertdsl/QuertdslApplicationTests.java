package study.quertdsl;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import study.quertdsl.entity.Hello;
import study.quertdsl.entity.QHello;

import static org.assertj.core.api.Assertions.*;

@Transactional
@SpringBootTest
class QuertdslApplicationTests {

	@Autowired
	EntityManager em;

	@Test
	void contextLoads() {
		Hello hello = new Hello();
		em.persist(hello);

		JPAQueryFactory query = new JPAQueryFactory(em);

		QHello qHello = QHello.hello;

		Hello result = (Hello) query.selectFrom(qHello)
				.fetchOne();
		assertThat(result).isEqualTo(hello);
	}

}
