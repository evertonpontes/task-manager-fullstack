import { create } from 'zustand';

type OrganizationModal = {
  isOpen: boolean;
  onOpen: () => void;
  onClose: () => void;
};

export const useOrganizationModal = create<OrganizationModal>((set) => ({
  isOpen: false,
  onOpen: () => set({ isOpen: true }),
  onClose: () => set({ isOpen: false }),
}));
