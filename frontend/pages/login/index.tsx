import React from 'react'

import { useWindowSize } from 'react-use'

import LoginForm from '@/components/_login/LoginForm'
import LoginTitleContainer from '@/components/_login/LoginTitleContainer'
import { UserInfo as UserInfoType } from '@/components/_user/type'
import useAlreadyLogin from '@/hook/useAlreadyLogin'
import { useSsrMe } from '@/hook/useSsrMe'
import { withAuthServerSideProps } from '@/server/withAuthServerSide'
import { CENTER_FLEX } from '@/styles/classNames'

type ServerSideProps = { me: UserInfoType }

const Login: React.FC<ServerSideProps> = ({ me }) => {
  useSsrMe(me)
  useAlreadyLogin()
  const { width } = useWindowSize()
  const big = width > 984
  return big ? (
    <div className={`w-full h-screen ${CENTER_FLEX}`}>
      <div className={'flex w-3/5 h-[150%] overflow-hidden'}>
        <LoginTitleContainer />
      </div>
      <div className={`flex w-2/5 ${CENTER_FLEX}`}>
        <LoginForm big={big} />
      </div>
    </div>
  ) : (
    <div className={`w-full h-screen ${CENTER_FLEX}`}>
      <LoginForm big={big} />
    </div>
  )
}

export const getServerSideProps = withAuthServerSideProps()

export default Login
