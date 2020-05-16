package com.board.service;

import java.util.List;

import com.board.domain.BoardDTO;

/**
 * 1. 등록, 수정 기능을 하는 insertBoard 메서드와 updateBoard 메서드를 registerBoard 메서드 하나로 처리하는 점
 * 2. registerBoard 메서드, deleteBoard 메서드의 리턴 타입을 boolean으로 처리하는 점
 * @author dudu
 *
 */
public interface BoardService {

	public boolean registerBoard(BoardDTO params);

	public BoardDTO getBoardDetail(Long idx);

	public boolean deleteBoard(Long idx);

	public List<BoardDTO> getBoardList();

}
