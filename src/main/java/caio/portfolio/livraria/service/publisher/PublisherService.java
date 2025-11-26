package caio.portfolio.livraria.service.publisher;

import org.springframework.stereotype.Service;

import caio.portfolio.livraria.infrastructure.entity.publisher.dto.CreatePublisherDTO;
import caio.portfolio.livraria.infrastructure.entity.publisher.dto.ResponsePublisherDTO;
import caio.portfolio.livraria.infrastructure.repository.PublisherRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PublisherService {

	private final PublisherRepository repo;

	public ResponsePublisherDTO createPublisher(CreatePublisherDTO dto) {
		// TODO 
		return null;
	}
}
