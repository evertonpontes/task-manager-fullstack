import { create } from 'zustand';

type TeamModal = {
  isOpen: boolean;
  onOpen: () => void;
  onClose: () => void;
};

export const useTeamModal = create<TeamModal>((set) => ({
  isOpen: false,
  onOpen: () => set({ isOpen: true }),
  onClose: () => set({ isOpen: false }),
}));
