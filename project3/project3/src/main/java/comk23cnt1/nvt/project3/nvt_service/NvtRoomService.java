package comk23cnt1.nvt.project3.nvt_service;

import comk23cnt1.nvt.project3.nvt_entity.NvtRoom;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface NvtRoomService {
    List<NvtRoom> findAll();
    NvtRoom findById(Long id);

    // create/update có upload ảnh
    NvtRoom create(NvtRoom room, MultipartFile imageFile);
    NvtRoom update(Long id, NvtRoom room, MultipartFile imageFile);

    void delete(Long id);
}
